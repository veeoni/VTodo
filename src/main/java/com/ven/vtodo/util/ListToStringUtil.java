package com.ven.vtodo.util;

import com.ven.vtodo.po.Permission;
import com.ven.vtodo.po.Tag;

import java.util.ArrayList;
import java.util.List;

public class ListToStringUtil {
    public static String tagsToIds(List<Tag> tags) {
        if (tags != null && !tags.isEmpty()) {
            StringBuilder ids = new StringBuilder();
            boolean flag = false;
            for (Tag t : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(t.getId());
            }
            return ids.toString();
        } else {
            return "";
        }
    }

    public static String permissionsToIds(List<Permission> permissions) {
        if (permissions != null && !permissions.isEmpty()) {
            StringBuilder ids = new StringBuilder();
            boolean flag = false;
            for (Permission p : permissions) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(p.getId());
            }
            return ids.toString();
        } else {
            return "";
        }
    }

    public static List<Long> idsToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (ids != null && !"".equals(ids)) {
            String[] idArray = ids.split(",");
            for (String s : idArray) {
                list.add(Long.valueOf(s));
            }
        }
        return list;
    }
}
