package com.common.vo;

import com.entity.sys.SysDept;
import com.entity.sys.SysMenu;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TreeVo树结构实体类
 */
@Data
public class TreeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private Long id;

//    private Long deptId;

    /** 节点名称 */
    private String label;

    /** 父级id */
    private Long parentId;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeVo> children;

    public TreeVo() { }

    public TreeVo(SysDept dept) {
        this.id = dept.getId();
//        this.deptId = dept.getId();
        this.label = dept.getDeptName();
        this.parentId = dept.getParentId();
        this.children = dept.getChildren().stream().map(TreeVo::new).collect(Collectors.toList());
    }

    public TreeVo(SysMenu menu) {
        this.id = menu.getId();
        this.label = menu.getName();
        this.parentId = menu.getParentId();
        this.children = menu.getChildren().stream().map(TreeVo::new).collect(Collectors.toList());
    }
}
