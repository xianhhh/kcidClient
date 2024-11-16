package xianhhh.Utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundUtils {
    public static final String BGM = "E:\\Desktop\\MCP-1.20.2-xianhhh\\src\\main\\resources\\pc\\sound.mp3";//bgm文件路径
    static Clip music = null; //声明Clip接口
    static File sourceFile = null; //声明文件变量

    public static void playSound(){
        playM(BGM);
    }

    /**
     * 音乐播放方法
     */
    public static void playM(String path){
        try {
            music = AudioSystem.getClip(); // 获取可用于播放音频文件或音频流的数据流
            sourceFile = new File(path);//获取文件
            AudioInputStream ais = AudioSystem.getAudioInputStream(sourceFile);//获得指示格式的音频输入流
            music.open(ais); //打开数据流
            music.start();    //开始播放音乐
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
