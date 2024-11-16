package net.minecraft.network;

import io.netty.util.Attribute;
import net.minecraft.network.protocol.Packet;

public interface ProtocolSwapHandler {
   static void swapProtocolIfNeeded(Attribute<ConnectionProtocol.CodecData<?>> p_301205_, Packet<?> p_301125_) {
      ConnectionProtocol connectionprotocol = p_301125_.nextProtocol();
      if (connectionprotocol != null) {
         ConnectionProtocol.CodecData<?> codecdata = p_301205_.get();
         ConnectionProtocol connectionprotocol1 = codecdata.protocol();
         if (connectionprotocol != connectionprotocol1) {
            ConnectionProtocol.CodecData<?> codecdata1 = connectionprotocol.codec(codecdata.flow());
            p_301205_.set(codecdata1);
         }
      }

   }
}