package org.landy.springtestdemo.domain;

/**
 * 用户领域模型
 *
 * @author Landy
 * @since 2018/01/18
 */
public class User {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
