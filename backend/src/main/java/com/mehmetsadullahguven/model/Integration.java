package com.mehmetsadullahguven.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "integration")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Integration extends BaseEntity{

    @Column(name = "title")
    private String title;

    @Column(name = "slug")
    private String slug;

    @Column(name = "base_url")
    private String baseUrl;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "r2s_product_list_id")
    private String r2sProductListId;

}
