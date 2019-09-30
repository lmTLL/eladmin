package me.zhengjie.modules.system.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author groot
 * @date 2019-09-27
 */
@Entity
@Data
@Table(name = "zw_sale_order_update")
public class ZwSaleOrderUpdate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "zw_sale_number", nullable = false)
    private String zwSaleNumber;

    @Column(name = "befores")
    private String befores;

    @Column(name = "nows")
    private String nows;

    @Column(name = "update_user")
    private String updateUser;
}