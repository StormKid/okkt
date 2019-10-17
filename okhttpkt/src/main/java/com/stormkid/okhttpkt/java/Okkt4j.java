package com.stormkid.okhttpkt.java;

import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.stormkid.okhttpkt.asyc.DownloadCallback;
import com.stormkid.okhttpkt.core.OkHttpClientBuilder;
import com.stormkid.okhttpkt.rule.ClientRule;
import com.stormkid.okhttpkt.rule.DownLoadRule;
import com.stormkid.okhttpkt.utils.CallbackNeed;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author ke_li
 * @date 2019/9/9
 */
public class Okkt4j {
    /**
     * 是否是工厂模式
     */
    private boolean isFactory = false;
    /**
     * 默认的clientType为单例模式
     */
    private String clientType = SINGLE_CLIENT;

    /**
     * 默认获取http对象
     */
    private String clientNetType = HTTP_TYPE;

    /**
     * 默认为http请求单例对象
     */
    private OkHttpClient okHttpClient = OkHttpClientBuilder.Builder.INSTANCE.build().getHttpClient().build();


    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * 设置baseUrl
     */
    private String baseUrl = "";

    /**
     * 设置错误指令，默认不处理
     */
    private String error = "网络链接失效，请检查网络连接";

    /**
     * 获取单例对象
     */
    public static final String SINGLE_CLIENT = "SINGLE_CLIENT";
    /**
     * 获取工厂对象
     */
    public static final String FACTORY_CLIENT = "FACTORY_CLIENT";

    /**
     * 获取http请求的OkHttpclient对象
     */

    public static final String HTTP_TYPE = "HTTP";
    /**
     * 获取https请求的OkHttpclient对象
     */

    public static final String HTTPS_TYPE = "HTTPS";

    /**
     * 获取自定义OkHttpclient对象
     */
    public static final String COMMOM_TYPE = "COMMOM_TYPE";

    private Okkt4j() {
    }

    private static Okkt4j okkt4j;

    public static final Okkt4j getInstance() {
        if (okkt4j == null) {
            synchronized (Okkt4j.class) {
                if (okkt4j == null)
                    okkt4j = new Okkt4j();
            }
        }
        return okkt4j;
    }

    public  Builder getBuilder(){
       return  new Builder();
    }

    /**
     * 设置获取的okhttpclient
     */
    public Okkt4j setClientType(String type) {
        if (type.equals(FACTORY_CLIENT) || type.equals(SINGLE_CLIENT))
            this.clientType = type;
        else this.clientType = SINGLE_CLIENT;
        return this;
    }

    /**
     * 设置请求方式，http请求，https请求或者自定义全局方式
     */
    public Okkt4j setNetClientType(String type) {
        if (type.equals(HTTPS_TYPE) || type.equals(HTTP_TYPE) || type.equals(COMMOM_TYPE))
            this.clientNetType = type;
        else this.clientType = HTTP_TYPE;
        return this;
    }

    /**
     * 可调用不init采取默认调用
     */
    public void initHttpClient() {
        initNetType(OkHttpClientBuilder.Builder.INSTANCE.build());
        if (clientType.equals(FACTORY_CLIENT)) {
            isFactory = true;
        }
    }

    /**
     * 获取相应的对象
     */
    private OkHttpClient getHttpClient() {
        if (isFactory) initNetType(OkHttpClientBuilder.Builder.INSTANCE.build());
        return okHttpClient;
    }

    private OkHttpClient getFactoryClient() {
        return OkHttpClientBuilder.Builder.INSTANCE.build().getHttpClient().build();
    }

    /**
     * 更新头部布局
     */
    public Okkt4j initHead(HashMap<String, String> map) {
        initNetType(OkHttpClientBuilder.Builder.INSTANCE.setHead(map).build());
        return this;
    }


    private void initNetType(ClientRule clientRule) {
        switch (clientNetType) {
            case HTTP_TYPE:
                okHttpClient = clientRule.getHttpClient().build();
                break;
            case HTTPS_TYPE:
                okHttpClient = clientRule.getHttpsClient().build();
                break;
            case COMMOM_TYPE:
                okHttpClient = clientRule.getCustomnClient().build();
                break;
        }
    }

    /**
     * 是否需要cookie
     */
    public Okkt4j isNeedCookie(boolean isNeed) {
        OkHttpClientBuilder.Builder.INSTANCE.build().isNeedCookie(isNeed);
        return this;
    }


    /**
     * 全部以long为单位输入请求超时时间
     */
    public Okkt4j setTimeOut(long time) {
        OkHttpClientBuilder.Builder.INSTANCE.build().setTimeOut(time);
        return this;
    }

    /**
     * 是否显示log
     */
    public Okkt4j isLogShow(boolean isLog) {
        OkHttpClientBuilder.Builder.INSTANCE.build().isLogShow(isLog);
        return this;
    }


    /**
     * 是否需要重定向
     */
    public Okkt4j isAllowRedirect(boolean isNeed) {
        OkHttpClientBuilder.Builder.INSTANCE.build().setFollowRedirects(isNeed);
        return this;
    }


    /**
     * 设置主体url
     */
    public Okkt4j setBase(String url) {
        this.baseUrl = url;
        return this;
    }


    /**
     * 设置错误信息
     */
    public Okkt4j setErr(String err) {
        this.error = err;
        return this;
    }

    private String initUrl(HashMap<String, String> map) {
        boolean first = false;
        String result = "";
        Set<String> keySet = map.keySet();
        for (String key :
                keySet) {
            String s = map.get(key);
            if (first) result += "&" + key + "=" + s;
            else {
                first = true;
                result += "?" + key + "=" + s;
            }
        }
        return result;
    }

    public class Builder {
        private BuildData data = new BuildData();

        /**
         * 输入url
         */
        public Builder setUrl(String url) {
            data.url = baseUrl + url;
            return this;
        }

        /**
         * 输入的全部url
         */
        public Builder setFullUrl(String url) {
            data.url = url;
            return this;
        }


        /**
         * 获取独有的请求标识,多连接的时候进行回调处理
         */
        public Builder setFlag(String flag) {
            data.flag = flag;
            return this;
        }

        /**
         * 输入请求body
         */
        public Builder putBody(HashMap<String, String> params) {
            data.body.clear();
            data.body.putAll(params);
            return this;
        }

        /**
         * 传入file
         */
        public Builder putFile(File file) {
            data.file = file;
            return this;
        }

        public Builder setFilePath(String filePath) {
            data.filePath = filePath;
            return this;
        }

        /**
         * 传入fileNameKey
         */
        public Builder putFileNameKey(String key) {
            data.fileNameKey = key;
            return this;
        }


        /**
         * 传入url拼接属性
         */
        public Builder setParams(HashMap<String, String> params) {
            data.params.clear();
            data.params.putAll(params);
            return this;
        }
        ////////////////////////////////////////////普通请求///////////////////////////////////////////////////////


        public <T> void get(CallbackRule<T> call) {
            requestInit(data, GET_TYPE).enqueue(new OkCallback(call, new CallbackNeed(data.flag, error)));
        }

        public <T> void post(CallbackRule<T> call) {
            requestInit(data, POST_FORM_TYPE).
                    enqueue(new OkCallback(call, new CallbackNeed(data.flag, error)));
        }

        public <T> void postJson(CallbackRule<T> call) {
            requestInit(data, POST_JSON_TYPE).
                    enqueue(new OkCallback(call, new CallbackNeed(data.flag, error)));
        }

        public <T> void postJson(String json, CallbackRule<T> call) {
            data.json = json;
            requestInit(data, POST_JSON_TYPE).
                    enqueue(new OkCallback(call, new CallbackNeed(data.flag, error)));
        }

        /**
         * 直传文件
         */
        public <T> void postFile(CallbackRule<T> call) {
            requestInit(data, FILE_UPLOAD).
                    enqueue(new OkCallback(call, new CallbackNeed(data.flag, error)));

        }


        private final String GET_TYPE = "GET_TYPE";
        private final String POST_FORM_TYPE = "POST_TYPE";
        private final String POST_JSON_TYPE = "POST_JSON_TYPE";
        private final String FILE_UPLOAD = "FILE_UPLOAD";

        private Call requestInit(BuildData data, String type) {
            String url = data.url + initUrl(data.params);
            Request.Builder builder = new Request.Builder().url(url);
            switch (type) {
                case GET_TYPE: {
                    Request request = builder.build();
                    return getHttpClient().newCall(request);

                }
                case POST_FORM_TYPE: {
                    FormBody.Builder bodyBuilder = new FormBody.Builder();
                    Set<String> keySet = data.body.keySet();
                    for (String key :
                            keySet) {
                        String s = data.body.get(key);
                        bodyBuilder.add(key, s);
                    }
                    FormBody requestBody = bodyBuilder.build();
                    return getHttpClient().newCall(builder.post(requestBody).build());

                }
                case POST_JSON_TYPE: {
                    String json = TextUtils.isEmpty(data.json) ? new Gson().toJson(data.body) : data.json;
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                    return getHttpClient().newCall(builder.post(requestBody).build());

                }

                case FILE_UPLOAD: {
                    if (data.file.exists()) {
                        MultipartBody.Builder partBuilder = new MultipartBody.Builder();
                        Set<String> keySet = data.body.keySet();
                        for (String key :
                                keySet) {
                            String s = data.body.get(key);
                            partBuilder.addFormDataPart(key, s);
                        }
                        partBuilder.addFormDataPart(data.fileNameKey, data.file.getName(), MultipartBody.create(MultipartBody.FORM, data.file));
                        MultipartBody multipartBody = partBuilder.build();
                        setTimeOut(60000);
                        return getFactoryClient().newCall(builder.post(multipartBody).build());
                    } else return null;
                }
            }
            return null;
        }

        /**
         * 系统下载器下载文件
         */
        public void download(String url, String title, String desc, Context context, DownLoadRule
                downloadRule) {
            Uri uri = Uri.parse(url);
            DownloadManager.Request req = new DownloadManager.Request(uri);
            //设置WIFI下进行更新
            req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            //下载中和下载完后都显示通知栏
            req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            //使用系统默认的下载路径 此处为应用内 /android/data/packages ,所以兼容7.0
            req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, title);
            //通知栏标题
            req.setTitle(title);
            //通知栏描述信息
            req.setDescription(desc);
            //设置类型为.apk
            req.setMimeType("application/vnd.android.package-archive");

            //获取下载任务ID
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            context.registerReceiver(new DownloadCallback(downloadRule), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            try {
                dm.enqueue(req);
            } catch (Exception e) {
                downloadRule.onNetErr();
            }

        }

        private class BuildData {
            HashMap<String, String> body = new HashMap<>();
            HashMap<String, String> params = new HashMap<>();
            String url = "";
            String json = "";
            File file = new File("");
            String filePath = "";
            String fileNameKey="";
            String flag = "";
        }

    }
}
