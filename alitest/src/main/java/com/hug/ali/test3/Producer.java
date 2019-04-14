package com.hug.ali.test3;

import java.io.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * 生产者
 */
public class Producer implements Callable<Boolean> {

    private DataBlockingQueue queue = DataBlockingQueue.getInstance();
    private CountDownLatch latch;
    private File file;

    public Producer(CountDownLatch latch, File file){
        this.latch = latch;
        this.file = file;
    }

    /**
     * 读取文件，存储到DataBlockingQueue中
     * @return
     * @throws IOException
     */
    @Override
    public Boolean call() throws IOException {
        InputStreamReader reader;
        BufferedReader br = null;
        try {
            reader = new InputStreamReader(new FileInputStream(new File(file.getPath())));
            br = new BufferedReader(reader);
            String line = null;
            while ((line = br.readLine()) != null) {
                queue.add(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        latch.countDown();
        return true;
    }


}