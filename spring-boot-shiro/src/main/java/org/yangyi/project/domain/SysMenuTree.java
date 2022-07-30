package org.yangyi.project.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenuTree extends SysMenu {

    private List<SysMenuTree> children;

    public List<SysMenuTree> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenuTree> children) {
        this.children = children;
    }

}
