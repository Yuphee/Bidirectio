package com.zhangyf.bidirectio.event;

/**
 * 图片加载完成事件
 *
 * @author zhangyf
 * @date 2018/5/16 0016.
 */
public class PicFirstLoadedEvent {
    public PicFirstLoadedEvent(int picSize, int articleId) {
        this.picSize = picSize;
        this.articleId = articleId;
    }

    public int picSize;

    public int articleId;
}
