package com.zhangyf.bidirectio.event;

/**
 * 图片加载完成事件
 *
 * @author zhangyf
 * @date 2018/5/16 0016.
 */
public class PicCachedEvent {

    public PicCachedEvent(int picSize, int articleId,int pos) {
        this.picSize = picSize;
        this.articleId = articleId;
        this.pos = pos;
    }

    public int picSize;

    public int articleId;

    public int pos;
}
