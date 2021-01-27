package pers.lomesome.ucm.client.view.MyUtils;

import pers.lomesome.ucm.client.tools.ManageMainGUI;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.net.URL;

public class BasicPlayer {

    private static AudioInputStream stream = null;
    private static AudioFormat format = null;
    private static SourceDataLine m_line;
    private static boolean flag = true;

    public static void play(String url) {
        if(flag){
            flag = false;
            try {
                URL filename = BasicPlayer.class.getClass().getResource(url);
                stream = AudioSystem.getAudioInputStream(filename);
                format = stream.getFormat();
                if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                    format = new AudioFormat(
                            AudioFormat.Encoding.PCM_SIGNED,
                            format.getSampleRate(),
                            16,
                            format.getChannels(),
                            format.getChannels() * 2,
                            format.getSampleRate(),
                            false);        // big endian
                    stream = AudioSystem.getAudioInputStream(format, stream);
                }

                DataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat(), AudioSystem.NOT_SPECIFIED);
                m_line = (SourceDataLine) AudioSystem.getLine(info);
                m_line.open(stream.getFormat(),m_line.getBufferSize());
                m_line.start();

                int numRead = 0;
                byte[] buf = new byte[m_line.getBufferSize()];
                while ((numRead = stream.read(buf, 0, buf.length)) >= 0) {
                    int offset = 0;
                    while (offset < numRead) {
                        offset += m_line.write(buf, offset, numRead-offset);
                    }
                }
                m_line.drain();
                m_line.stop();
                m_line.close();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
            flag = true;
        }
    }

    public void sound(String url){
        if(flag && ManageMainGUI.getMainGui().isMusic())
            new MyThread(url).start();
    }

    class MyThread extends Thread {

        private String url;

        public MyThread(String url){
            this.url = url;
        }

        @Override
        public void run() {
            BasicPlayer.play(url);
        }
    }
}
