package pers.lomesome.ucm.client.tools;

/**
 * 功能：消息图片压缩
 */

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;

public class ImageZip {
    public ImageZip() {

    }

    public String imageToBase64(BufferedImage bufferedImage, String filename) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, filename , bos);
            byte[] imageBytes = bos.toByteArray();
            Encoder encoder = Base64.getEncoder();   // 对字节数组Base64编码
            imageString = encoder.encodeToString(imageBytes);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    public String resizeImageTo500K(String path) throws IOException {
        BufferedImage src = ImageIO.read(new FileInputStream(path));
        String filename = path.substring(path.lastIndexOf(".")+1);
        BufferedImage output = Thumbnails.of(src).scale(1).outputQuality(0.5f).asBufferedImage();
        String base64 = imageToBase64(output,filename);
        while (base64.length() - base64.length() / 8 * 2 > 600000) {
            output = Thumbnails.of(output).scale(0.9).outputQuality(0.25).asBufferedImage();
            base64 = imageToBase64(output,filename);
        }
        return base64;
    }

    public String resizeImageToSmall(String path) throws IOException {
        BufferedImage src = ImageIO.read(new FileInputStream(path));
        String filename = path.substring(path.lastIndexOf(".")+1);
        BufferedImage output = Thumbnails.of(src).scale(1).outputQuality(0.5f).asBufferedImage();
        String base64 = imageToBase64(output,filename);
        while (base64.length() - base64.length() / 8 * 2 > 40000) {
            output = Thumbnails.of(output).scale(0.9).outputQuality(0.25).asBufferedImage();
            base64 = imageToBase64(output,filename);
        }
        return base64;
    }
}
