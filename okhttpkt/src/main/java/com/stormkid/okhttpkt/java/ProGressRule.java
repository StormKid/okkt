package com.stormkid.okhttpkt.java;

import java.io.File;

/**
 * @author ke_li
 * @date 2019/9/9
 */
public interface ProGressRule {
     public void getProgress( int progress);

     public void onFinished();

     public void onStartRequest();

     public void onOpenFile(File file);
}
