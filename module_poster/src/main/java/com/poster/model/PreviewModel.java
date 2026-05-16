package com.poster.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2022/1/31
 */

public class PreviewModel {
    /**
     * template : {"owner":0,"isbuy":0,"buyCount":0,"browseCount":0,"icon":"eb8becaaa39c440a9a3f98e8ce6be2f6","description":"10","createdate":"2017-03-21 16:37:13","viewtype":null,"type":5,"tid":84,"pages":[{"objects":[{"img":null,"color":null,"fontsize":null,"weight":null,"createdate":"2017-03-21 16:37:13","pid":148,"oid":580,"type":1,"transparency":0,"width":1280,"x":0,"y":0,"z":0,"text":null,"height":643,"font":null},{"img":"3ab113892054485a8c8b8e4aa0b581e3","color":"#000000","fontsize":63,"weight":null,"createdate":"2017-03-21 16:37:13","pid":148,"oid":581,"type":2,"transparency":null,"width":null,"x":100,"y":100,"z":2,"text":"你在哪里","height":null,"font":"微软雅黑"}],"icon":"e67d890bbba046c1b90f4441b9e7dd82","description":"0","createdate":"2017-03-21 16:37:13","pid":148,"type":5,"tid":84,"editZ":0,"price":0,"pageno":1,"name":"0","width":1280,"height":643,"group":null},{"objects":[{"img":null,"color":null,"fontsize":null,"weight":null,"createdate":"2017-03-21 16:37:13","pid":149,"oid":582,"type":1,"transparency":0,"width":100,"x":0,"y":0,"z":0,"text":null,"height":100,"font":null},{"img":"3ab113892054485a8c8b8e4aa0b581e3","color":"#000000","fontsize":35,"weight":null,"createdate":"2017-03-21 16:37:13","pid":149,"oid":583,"type":2,"transparency":null,"width":null,"x":200,"y":200,"z":1,"text":"我在这里","height":null,"font":"微软雅黑"}],"icon":"e67d890bbba046c1b90f4441b9e7dd82","description":"1","createdate":"2017-03-21 16:37:13","pid":149,"type":5,"tid":84,"editZ":0,"price":1,"pageno":1,"name":"1","width":100,"height":100,"group":null}],"price":10,"name":"10","width":1280,"listtype":null,"useCount":0,"height":643,"order":0,"status":0}
     * result : 01
     * msg : 成功！
     */

    private TemplateBean template;
    private String result;
    private String msg;

    public TemplateBean getTemplate() {
        return template;
    }

    public void setTemplate(TemplateBean pages) {
        this.template = pages;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class TemplateBean {
        /**
         * owner : 0
         * isbuy : 0
         * buyCount : 0
         * browseCount : 0
         * icon : eb8becaaa39c440a9a3f98e8ce6be2f6
         * description : 10
         * createdate : 2017-03-21 16:37:13
         * viewtype : null
         * type : 5
         * tid : 84
         * pages : [{"objects":[{"img":null,"color":null,"fontsize":null,"weight":null,"createdate":"2017-03-21 16:37:13","pid":148,"oid":580,"type":1,"transparency":0,"width":1280,"x":0,"y":0,"z":0,"text":null,"height":643,"font":null},{"img":"3ab113892054485a8c8b8e4aa0b581e3","color":"#000000","fontsize":63,"weight":null,"createdate":"2017-03-21 16:37:13","pid":148,"oid":581,"type":2,"transparency":null,"width":null,"x":100,"y":100,"z":2,"text":"你在哪里","height":null,"font":"微软雅黑"}],"icon":"e67d890bbba046c1b90f4441b9e7dd82","description":"0","createdate":"2017-03-21 16:37:13","pid":148,"type":5,"tid":84,"editZ":0,"price":0,"pageno":1,"name":"0","width":1280,"height":643,"group":null},{"objects":[{"img":null,"color":null,"fontsize":null,"weight":null,"createdate":"2017-03-21 16:37:13","pid":149,"oid":582,"type":1,"transparency":0,"width":100,"x":0,"y":0,"z":0,"text":null,"height":100,"font":null},{"img":"3ab113892054485a8c8b8e4aa0b581e3","color":"#000000","fontsize":35,"weight":null,"createdate":"2017-03-21 16:37:13","pid":149,"oid":583,"type":2,"transparency":null,"width":null,"x":200,"y":200,"z":1,"text":"我在这里","height":null,"font":"微软雅黑"}],"icon":"e67d890bbba046c1b90f4441b9e7dd82","description":"1","createdate":"2017-03-21 16:37:13","pid":149,"type":5,"tid":84,"editZ":0,"price":1,"pageno":1,"name":"1","width":100,"height":100,"group":null}]
         * price : 10
         * name : 10
         * width : 1280
         * listtype : null
         * useCount : 0
         * uid : 0
         * height : 643
         * order : 0
         * mname : 黄金会员
         * micon
         * status : 0
         */

        private int owner;
        private int isbuy;
        private int buyCount;
        private int browseCount;
        private String icon;
        private String description;
        private String createdate;
        private Integer viewtype;
        private String micon;
        private String mname;
        private int type;
        private int tid;
        private int price;
        private int uid;
        private String name;
        private int width;
        private Integer listtype;
        private int useCount;
        private int height;
        private int order;
        private int status;
        private int isup;

        public String getMicon() {
            return micon;
        }

        public void setMicon(String micon) {
            this.micon = micon;
        }

        public String getMname() {
            return mname;
        }

        public void setMname(String mname) {
            this.mname = mname;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getIsup() {
            return isup;
        }

        public void setIsup(int isup) {
            this.isup = isup;
        }

        private List<PagesBean> pages;

        public int getOwner() {
            return owner;
        }

        public void setOwner(int owner) {
            this.owner = owner;
        }

        public int getIsbuy() {
            return isbuy;
        }

        public void setIsbuy(int isbuy) {
            this.isbuy = isbuy;
        }

        public int getBuyCount() {
            return buyCount;
        }

        public void setBuyCount(int buyCount) {
            this.buyCount = buyCount;
        }

        public int getBrowseCount() {
            return browseCount;
        }

        public void setBrowseCount(int browseCount) {
            this.browseCount = browseCount;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCreatedate() {
            return createdate;
        }

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }

        public Integer getViewtype() {
            return viewtype;
        }

        public void setViewtype(Integer viewtype) {
            this.viewtype = viewtype;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public Integer getListtype() {
            return listtype;
        }

        public void setListtype(Integer listtype) {
            this.listtype = listtype;
        }

        public int getUseCount() {
            return useCount;
        }

        public void setUseCount(int useCount) {
            this.useCount = useCount;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<PagesBean> getPages() {
            return pages;
        }

        public void setPages(List<PagesBean> pages) {
            this.pages = pages;
        }

        public static class PagesBean implements Serializable {
            /**
             * objects : [{"img":null,"color":null,"fontsize":null,"weight":null,"createdate":"2017-03-21 16:37:13","pid":148,"oid":580,"type":1,"transparency":0,"width":1280,"x":0,"y":0,"z":0,"text":null,"height":643,"font":null},{"img":"3ab113892054485a8c8b8e4aa0b581e3","color":"#000000","fontsize":63,"weight":null,"createdate":"2017-03-21 16:37:13","pid":148,"oid":581,"type":2,"transparency":null,"width":null,"x":100,"y":100,"z":2,"text":"你在哪里","height":null,"font":"微软雅黑"}]
             * icon : e67d890bbba046c1b90f4441b9e7dd82
             * description : 0
             * createdate : 2017-03-21 16:37:13
             * pid : 148
             * type : 5
             * tid : 84
             * editZ : 0.0
             * price : 0
             * pageno : 1
             * name : 0
             * width : 1280
             * height : 643
             * group : null
             * id   : 10001
             */

            private String icon;
            private String description;
            private String createdate;
            private int pid;
            private int type;
            private int tid;
            private Integer editZ;
            private int price;
            private int pageno;
            private String name;
            private int width;
            private int height;
            private int id;
            private String groupno;
            private List<ObjectsBean> objects;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getCreatedate() {
                return createdate;
            }

            public void setCreatedate(String createdate) {
                this.createdate = createdate;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getTid() {
                return tid;
            }

            public void setTid(int tid) {
                this.tid = tid;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Integer getEditZ() {
                return editZ;
            }

            public void setEditZ(Integer editZ) {
                this.editZ = editZ;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public int getPageno() {
                return pageno;
            }

            public void setPageno(int pageno) {
                this.pageno = pageno;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public String getGroup() {
                return groupno;
            }

            public void setGroup(String group) {
                this.groupno = group;
            }

            public List<ObjectsBean> getObjects() {
                return objects;
            }

            public void setObjects(List<ObjectsBean> objects) {
                this.objects = objects;
            }

            public static class ObjectsBean implements Serializable {
                /**
                 * img : null
                 * color : null
                 * fontsize : null
                 * weight : null
                 * createdate : 2017-03-21 16:37:13
                 * pid : 148
                 * oid : 580
                 * type : 1
                 * zoom :1
                 * transparency : 0
                 * width : 1280
                 * x : 0
                 * y : 0
                 * z : 0
                 * angle 角度
                 * scale 比例
                 * text : null
                 * height : 643
                 * font : null
                 * id :100001    唯一的
                 */

                private String img;
                private String color;
                private String fontsize;
                private String weight;
                private String createdate;
                private float angle;
                private float scale;
                private int pid;
                private int oid;
                private int type;                        //0:文本         1：图片       2：动画     4：视频
                private int transparency;
                private int zoom;
                private int width;
                private int x;
                private int replaceable;
                private int y;
                private int z;
                private String text;
                private int height;
                private String font;
                private int id;
                private float density;               //手机屏幕密度，用于绘制字体

                public float getDensity() {
                    return density;
                }

                public void setDensity(float density) {
                    this.density = density;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public float getAngle() {
                    return angle;
                }

                public void setAngle(float angle) {
                    this.angle = angle;
                }

                public float getScale() {
                    return scale;
                }

                public void setScale(float scale) {
                    this.scale = scale;
                }

                public int getZoom() {
                    return zoom;
                }

                public void setZoom(int zoom) {
                    this.zoom = zoom;
                }

                public int getReplaceable() {
                    return replaceable;
                }

                public void setReplaceable(int replaceable) {
                    this.replaceable = replaceable;
                }

                public void setMatrixBeans(float[] matrixBeans) {
                    this.matrixBeans = matrixBeans;
                }

                public float[] getMatrixBeans() {
                    return matrixBeans;
                }

                private float[] matrixBeans;

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getColor() {
                    return color;
                }

                public void setColor(String color) {
                    this.color = color;
                }

                public String getFontsize() {
                    return fontsize;
                }

                public void setFontsize(String fontsize) {
                    this.fontsize = fontsize;
                }

                public String getWeight() {
                    return weight;
                }

                public void setWeight(String weight) {
                    this.weight = weight;
                }

                public String getCreatedate() {
                    return createdate;
                }

                public void setCreatedate(String createdate) {
                    this.createdate = createdate;
                }

                public int getPid() {
                    return pid;
                }

                public void setPid(int pid) {
                    this.pid = pid;
                }

                public int getOid() {
                    return oid;
                }

                public void setOid(int oid) {
                    this.oid = oid;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public int getTransparency() {
                    return transparency;
                }

                public void setTransparency(int transparency) {
                    this.transparency = transparency;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getX() {
                    return x;
                }

                public void setX(int x) {
                    this.x = x;
                }

                public int getY() {
                    return y;
                }

                public void setY(int y) {
                    this.y = y;
                }

                public int getZ() {
                    return z;
                }

                public void setZ(int z) {
                    this.z = z;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public String getFont() {
                    return font;
                }

                public void setFont(String font) {
                    this.font = font;
                }
            }
        }
    }
}
