package com.example.factory.net;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.example.commom.app.Application;
import com.example.commom.utils.HashUtil;
import com.example.factory.Factory;

import java.io.File;
import java.util.Date;

/**
 * 上传到oss对象存储帮助类
 *
 * Created by wenjian on 2017/6/9.
 */

public class UploadHelper {

    private static final String TAG = "UploadHelper";

    private static final String ENDPOINT = "http://oss-cn-shanghai.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAIMJuvY6VC4UY1";
    private static final String ACCESS_KEY_SECRET = "hQek4RqQEsCm9i8Py8y1hGeT2yv0Jh";
    private static final String BUCKET_NAME = "talker";


    public static OSSClient getClient() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        return new OSSClient(Factory.app(), ENDPOINT, credentialProvider);
    }

    /**
     * 上传
     * @param objectKey key
     * @param uploadFilePath 文件路径
     * @return 服务器存储地址
     */
    private static String upload(String objectKey, String uploadFilePath) {
        // 构造上传请求
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objectKey, uploadFilePath);

        try {
            OSSClient client = getClient();

            client.putObject(request);
            String url = client.presignPublicObjectURL(BUCKET_NAME, objectKey);
            Log.i(TAG, "presignPublicObjectURL: " + url);
            return url;

        } catch (Exception e) {
            // 本地异常如网络异常等
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 上传普通图片
     * @param path 本地路径
     * @return 服务器路径
     */
    public static String uploadImage(String path) {
        String imageKey = createImageKey(path);
        return upload(imageKey, path);
    }

    /**
     * 上传头像
     * @param path 本地路径
     * @return 服务器路径
     */
    public static String uploadPortrait(String path) {
        String imageKey = createPortraitKey(path);
        return upload(imageKey, path);
    }

    /**
     * 上传音频
     * @param path 本地路径
     * @return 服务器路径
     */
    public static String uploadAudio(String path) {
        String imageKey = createAudioKey(path);
        return upload(imageKey, path);
    }


    /**
     * 创建图片存储键
     * @param path 本地路径
     * @return image/201707/2d92dd0adjadpaf.jpg
     */
    private static String createImageKey(String path) {
        String md5String = HashUtil.getMD5String(path);
        String dateString = getDateString();
        return String.format("image/%s/%s.jpg", dateString, md5String);
    }


    /**
     * 创建头像存储键
     * @param path 本地路径
     * @return portrait/201707/2d92dd0adjadpaf.jpg
     */
    private static String createPortraitKey(String path) {
        String md5String = HashUtil.getMD5String(path);
        String dateString = getDateString();
        return String.format("portrait/%s/%s.jpg", dateString, md5String);
    }



    /**
     * 创建音频存储键
     * @param path 本地路径
     * @return audio/201707/2d92dd0adjadpaf.mp3
     */
    private static String createAudioKey(String path) {
        String md5String = HashUtil.getMD5String(path);
        String dateString = getDateString();
        return String.format("audio/%s/%s.mp3", dateString, md5String);
    }

    /**
     * 按月存储
     * @return 格式化的日期字符串,形如:201705
     */
    private static String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    /**
     * 获取语音本地存储路径
     * @param isTemp 是否是临时的
     * @return
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String getAudioPath(boolean isTemp) {
        File dir = new File(Application.getInstance().getCacheDir(), "audio");
        dir.mkdir();
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }
        File audioFile = new File(dir, isTemp ? "temp.mp3" : System.currentTimeMillis() + ".mp3");
        return audioFile.getAbsolutePath();
    }


}
