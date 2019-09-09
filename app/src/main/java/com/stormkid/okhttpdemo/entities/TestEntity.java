package com.stormkid.okhttpdemo.entities;

import java.io.Serializable;
import java.util.List;

/**
 * @author ke_li
 * @date 2019/9/9
 */
public class TestEntity implements Serializable {

    /**
     * msg : success
     * code : 1
     * data : [{"imgextra":[{"imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/45db013761054f0a9c6346c2d5c216b2.png"},{"imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/6a10ed381aa7481faaea21fe0a1533c7.png"}],"docid":"EOKBH3IO04178D6R","source":"南方都市报","title":"抱娃男子被另一男子挥刀追砍 当场倒地","priority":250,"hasImg":1,"url":"http://3g.163.com/ntes/19/0909/08/EOKBH3IO04178D6R.html","commentCount":642,"imgsrc3gtype":"2","stitle":"","digest":"有网友报料称，9月8日傍晚6点左右，在深圳宝安区沙井新沙路发","imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/27c4b5b21f274e8286f994f46835e774.png","ptime":"2019-09-09 08:35:24"},{"imgsrc3gtype":"1","stitle":"","docid":"EOL3NDG504178D6R","digest":"来源：央视新闻9月5日，在广东深圳龙岗区，一车主将车在公共绿","source":"读特","imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/125bfe46eb6146a6b488aaa768447f99.png","title":"深圳一车主停车绿化带被罚3230元 罚金是怎么算的？","priority":110,"ptime":"2019-09-09 15:38:17","hasImg":1,"url":"http://3g.163.com/ntes/19/0909/15/EOL3NDG504178D6R.html","commentCount":2},{"imgsrc3gtype":"1","stitle":"","docid":"EOL6UJ8S04178D6R","digest":"近日，深圳市人大常委会修改《深圳经济特区道路交通安全违法行为","source":"环球网","imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/0aa837b487da4fc1a7fd1f7cb6259214.png","title":"深圳拟出新规：开车时玩手机将罚300元，11月实施","priority":108,"ptime":"2019-09-09 16:34:38","hasImg":1,"url":"http://3g.163.com/ntes/19/0909/16/EOL6UJ8S04178D6R.html","commentCount":1},{"imgsrc3gtype":"1","stitle":"","docid":"EOKEPIM104178D6R","digest":"深圳铁路枢纽城市建设再提速。根据9月6日，深圳市交通运输管理","source":"南方都市报","imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/bd79af22532a4135adf4b6a0ba78b28c.png","title":"龙华或添新地标!深圳高铁总部拟落户北站商务中心区","priority":92,"ptime":"2019-09-09 09:32:27","hasImg":1,"url":"http://3g.163.com/ntes/19/0909/09/EOKEPIM104178D6R.html","commentCount":74},{"imgsrc3gtype":"1","stitle":"","docid":"EOKVHL7004178D6R","digest":"来源：深圳天气深圳市气象台于2019年09月09日14时10","source":"深圳天气","imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/456a249593d2448984342cdd64534915.png","title":"深圳发布雷电预警！预计1小时内全市将受雷电影响","priority":90,"ptime":"2019-09-09 14:25:14","hasImg":1,"url":"http://3g.163.com/ntes/19/0909/14/EOKVHL7004178D6R.html","commentCount":4},{"imgsrc3gtype":"1","stitle":"","docid":"EOKEH90K04178D6R","digest":"近日，家住深圳市宝安区华强城小区的业主向南都记者反映，一到晚","source":"南方都市报","imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/0027acd8b12048da826e2afb09243525.png","title":"深圳一网红盘住户称老被刺鼻气味熏醒 疑工厂排废气","priority":90,"ptime":"2019-09-09 09:27:55","hasImg":1,"url":"http://3g.163.com/ntes/19/0909/09/EOKEH90K04178D6R.html","commentCount":94},{"imgextra":[{"imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/fda261919b784d30a5bb170ded82cb89.png"},{"imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/6b0723c1834f47318872b61a156f2c9b.png"}],"docid":"EOKETPJE04178D6R","source":"南方都市报","title":"深圳一蓝的刮碰3辆车后冲向人行道，2人被撞","priority":89,"hasImg":1,"url":"http://3g.163.com/ntes/19/0909/09/EOKETPJE04178D6R.html","commentCount":470,"imgsrc3gtype":"2","stitle":"","digest":"据深圳交警官方微博发布\u201c突发事故\u201d称，9月8日15时40分许","imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/d00c1d7c503849a6b48c09300aea7e0b.png","ptime":"2019-09-09 09:34:46"},{"imgsrc3gtype":"1","stitle":"","docid":"EOKV101L04178D6R","digest":"9日上午，有网友报料称，宝安西乡一电动车充电站发生火灾。记者","source":"读特","imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/9853c772fd9749e0b33f6fc3a87740ff.png","title":"宝安一电动车充电站起火 60多辆电动车被烧毁","priority":88,"ptime":"2019-09-09 14:16:08","hasImg":1,"url":"http://3g.163.com/ntes/19/0909/14/EOKV101L04178D6R.html","commentCount":18},{"imgsrc3gtype":"1","stitle":"","docid":"EOKG25IQ04178D6R","digest":"来源：深圳地铁中秋节地铁将延长运营服务时间增开上线列车201","source":"深圳晚报","imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/a0a78252c3b6423887e7edcbaf6af386.png","title":"中秋节深圳地铁运营延长至24:00 将加开列车","priority":88,"ptime":"2019-09-09 09:54:38","hasImg":1,"url":"http://3g.163.com/ntes/19/0909/09/EOKG25IQ04178D6R.html","commentCount":119},{"imgsrc3gtype":"1","stitle":"","docid":"EOKEC2DE04178D6R","digest":"作为一个外来人口居多的城市，深圳哪里人最多？有人会告诉你：\u201c","source":"深圳特区报","imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/bebf26aea9b246cd8e035a7924c2546b.png","title":"时速350km！龙岗到汕尾或将再添一条新高铁","priority":88,"ptime":"2019-09-09 09:25:05","hasImg":1,"url":"http://3g.163.com/ntes/19/0909/09/EOKEC2DE04178D6R.html","commentCount":59}]
     */

    private String msg;
    private int code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * imgextra : [{"imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/45db013761054f0a9c6346c2d5c216b2.png"},{"imgsrc":"http://cms-bucket.ws.126.net/2019/09/09/6a10ed381aa7481faaea21fe0a1533c7.png"}]
         * docid : EOKBH3IO04178D6R
         * source : 南方都市报
         * title : 抱娃男子被另一男子挥刀追砍 当场倒地
         * priority : 250
         * hasImg : 1
         * url : http://3g.163.com/ntes/19/0909/08/EOKBH3IO04178D6R.html
         * commentCount : 642
         * imgsrc3gtype : 2
         * stitle :
         * digest : 有网友报料称，9月8日傍晚6点左右，在深圳宝安区沙井新沙路发
         * imgsrc : http://cms-bucket.ws.126.net/2019/09/09/27c4b5b21f274e8286f994f46835e774.png
         * ptime : 2019-09-09 08:35:24
         */

        private String docid;
        private String source;
        private String title;
        private int priority;
        private int hasImg;
        private String url;
        private int commentCount;
        private String imgsrc3gtype;
        private String stitle;
        private String digest;
        private String imgsrc;
        private String ptime;
        private List<ImgextraBean> imgextra;

        public String getDocid() {
            return docid;
        }

        public void setDocid(String docid) {
            this.docid = docid;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public int getHasImg() {
            return hasImg;
        }

        public void setHasImg(int hasImg) {
            this.hasImg = hasImg;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public String getImgsrc3gtype() {
            return imgsrc3gtype;
        }

        public void setImgsrc3gtype(String imgsrc3gtype) {
            this.imgsrc3gtype = imgsrc3gtype;
        }

        public String getStitle() {
            return stitle;
        }

        public void setStitle(String stitle) {
            this.stitle = stitle;
        }

        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public String getImgsrc() {
            return imgsrc;
        }

        public void setImgsrc(String imgsrc) {
            this.imgsrc = imgsrc;
        }

        public String getPtime() {
            return ptime;
        }

        public void setPtime(String ptime) {
            this.ptime = ptime;
        }

        public List<ImgextraBean> getImgextra() {
            return imgextra;
        }

        public void setImgextra(List<ImgextraBean> imgextra) {
            this.imgextra = imgextra;
        }

        public static class ImgextraBean {
            /**
             * imgsrc : http://cms-bucket.ws.126.net/2019/09/09/45db013761054f0a9c6346c2d5c216b2.png
             */

            private String imgsrc;

            public String getImgsrc() {
                return imgsrc;
            }

            public void setImgsrc(String imgsrc) {
                this.imgsrc = imgsrc;
            }
        }
    }
}
