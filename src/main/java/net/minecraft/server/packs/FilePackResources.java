package net.minecraft.server.packs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.IoSupplier;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

public class FilePackResources extends AbstractPackResources {
   static final Logger LOGGER = LogUtils.getLogger();
   private final FilePackResources.SharedZipFileAccess zipFileAccess;
   private final String prefix;

   FilePackResources(String p_256076_, FilePackResources.SharedZipFileAccess p_298373_, boolean p_256556_, String p_298638_) {
      super(p_256076_, p_256556_);
      this.zipFileAccess = p_298373_;
      this.prefix = p_298638_;
   }

   private static String getPathFromLocation(PackType p_250585_, ResourceLocation p_251470_) {
      return String.format(Locale.ROOT, "%s/%s/%s", p_250585_.getDirectory(), p_251470_.getNamespace(), p_251470_.getPath());
   }

   @Nullable
   public IoSupplier<InputStream> getRootResource(String... p_248514_) {
      return this.getResource(String.join("/", p_248514_));
   }

   public IoSupplier<InputStream> getResource(PackType p_249605_, ResourceLocation p_252147_) {
      return this.getResource(getPathFromLocation(p_249605_, p_252147_));
   }

   private String addPrefix(String p_299206_) {
      return this.prefix.isEmpty() ? p_299206_ : this.prefix + "/" + p_299206_;
   }

   @Nullable
   private IoSupplier<InputStream> getResource(String p_251795_) {
      ZipFile zipfile = this.zipFileAccess.getOrCreateZipFile();
      if (zipfile == null) {
         return null;
      } else {
         ZipEntry zipentry = zipfile.getEntry(this.addPrefix(p_251795_));
         return zipentry == null ? null : IoSupplier.create(zipfile, zipentry);
      }
   }

   public Set<String> getNamespaces(PackType p_10238_) {
      ZipFile zipfile = this.zipFileAccess.getOrCreateZipFile();
      if (zipfile == null) {
         return Set.of();
      } else {
         Enumeration<? extends ZipEntry> enumeration = zipfile.entries();
         Set<String> set = Sets.newHashSet();
         String s = this.addPrefix(p_10238_.getDirectory() + "/");

         while(enumeration.hasMoreElements()) {
            ZipEntry zipentry = enumeration.nextElement();
            String s1 = zipentry.getName();
            String s2 = extractNamespace(s, s1);
            if (!s2.isEmpty()) {
               if (ResourceLocation.isValidNamespace(s2)) {
                  set.add(s2);
               } else {
                  LOGGER.warn("Non [a-z0-9_.-] character in namespace {} in pack {}, ignoring", s2, this.zipFileAccess.file);
               }
            }
         }

         return set;
      }
   }

   @VisibleForTesting
   public static String extractNamespace(String p_298682_, String p_300360_) {
      if (!p_300360_.startsWith(p_298682_)) {
         return "";
      } else {
         int i = p_298682_.length();
         int j = p_300360_.indexOf(47, i);
         return j == -1 ? p_300360_.substring(i) : p_300360_.substring(i, j);
      }
   }

   public void close() {
      this.zipFileAccess.close();
   }

   public void listResources(PackType p_250500_, String p_249598_, String p_251613_, PackResources.ResourceOutput p_250655_) {
      ZipFile zipfile = this.zipFileAccess.getOrCreateZipFile();
      if (zipfile != null) {
         Enumeration<? extends ZipEntry> enumeration = zipfile.entries();
         String s = this.addPrefix(p_250500_.getDirectory() + "/" + p_249598_ + "/");
         String s1 = s + p_251613_ + "/";

         while(enumeration.hasMoreElements()) {
            ZipEntry zipentry = enumeration.nextElement();
            if (!zipentry.isDirectory()) {
               String s2 = zipentry.getName();
               if (s2.startsWith(s1)) {
                  String s3 = s2.substring(s.length());
                  ResourceLocation resourcelocation = ResourceLocation.tryBuild(p_249598_, s3);
                  if (resourcelocation != null) {
                     p_250655_.accept(resourcelocation, IoSupplier.create(zipfile, zipentry));
                  } else {
                     LOGGER.warn("Invalid path in datapack: {}:{}, ignoring", p_249598_, s3);
                  }
               }
            }
         }

      }
   }

   public static class FileResourcesSupplier implements Pack.ResourcesSupplier {
      private final File content;
      private final boolean isBuiltin;

      public FileResourcesSupplier(Path p_301133_, boolean p_297682_) {
         this(p_301133_.toFile(), p_297682_);
      }

      public FileResourcesSupplier(File p_299311_, boolean p_298123_) {
         this.isBuiltin = p_298123_;
         this.content = p_299311_;
      }

      public PackResources openPrimary(String p_297270_) {
         FilePackResources.SharedZipFileAccess filepackresources$sharedzipfileaccess = new FilePackResources.SharedZipFileAccess(this.content);
         return new FilePackResources(p_297270_, filepackresources$sharedzipfileaccess, this.isBuiltin, "");
      }

      public PackResources openFull(String p_299586_, Pack.Info p_299388_) {
         FilePackResources.SharedZipFileAccess filepackresources$sharedzipfileaccess = new FilePackResources.SharedZipFileAccess(this.content);
         PackResources packresources = new FilePackResources(p_299586_, filepackresources$sharedzipfileaccess, this.isBuiltin, "");
         List<String> list = p_299388_.overlays();
         if (list.isEmpty()) {
            return packresources;
         } else {
            List<PackResources> list1 = new ArrayList<>(list.size());

            for(String s : list) {
               list1.add(new FilePackResources(p_299586_, filepackresources$sharedzipfileaccess, this.isBuiltin, s));
            }

            return new CompositePackResources(packresources, list1);
         }
      }
   }

   static class SharedZipFileAccess implements AutoCloseable {
      final File file;
      @Nullable
      private ZipFile zipFile;
      private boolean failedToLoad;

      SharedZipFileAccess(File p_300196_) {
         this.file = p_300196_;
      }

      @Nullable
      ZipFile getOrCreateZipFile() {
         if (this.failedToLoad) {
            return null;
         } else {
            if (this.zipFile == null) {
               try {
                  this.zipFile = new ZipFile(this.file);
               } catch (IOException ioexception) {
                  FilePackResources.LOGGER.error("Failed to open pack {}", this.file, ioexception);
                  this.failedToLoad = true;
                  return null;
               }
            }

            return this.zipFile;
         }
      }

      public void close() {
         if (this.zipFile != null) {
            IOUtils.closeQuietly((Closeable)this.zipFile);
            this.zipFile = null;
         }

      }

      protected void finalize() throws Throwable {
         this.close();
         super.finalize();
      }
   }
}