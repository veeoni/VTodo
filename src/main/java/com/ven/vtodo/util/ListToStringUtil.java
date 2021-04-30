package com.ven.vtodo.util;

import com.ven.vtodo.po.Tag;

import java.util.List;

public class ListToStringUtil {
    public static String tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuilder ids = new StringBuilder();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return "";
        }
    }
}
