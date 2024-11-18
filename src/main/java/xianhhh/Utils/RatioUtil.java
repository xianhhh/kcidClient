package xianhhh.Utils;

/**
 * @author Kalud
 * @website pixelskider.github.io/
 * @since 2024/11/18
 * 用来绘制图片
 */
public class RatioUtil {
    private SizeUtil realSize,fakeSize;
    private int widthRatio,heightRatio;
    public RatioUtil(SizeUtil realSize,SizeUtil fakeSize){
        this.realSize = realSize;
        this.fakeSize = fakeSize;
        this.widthRatio = fakeSize.getWidth() / realSize.getWidth();
        this.heightRatio = fakeSize.getHeight() / realSize.getHeight();
    }

    public SizeUtil getRealSize() {
        return realSize;
    }

    public SizeUtil getFakeSize() {
        return fakeSize;
    }

    public int getWidthRatio() {
        return widthRatio;
    }

    public int getHeightRatio() {
        return heightRatio;
    }
}
