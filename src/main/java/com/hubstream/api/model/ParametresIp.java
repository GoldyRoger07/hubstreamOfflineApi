package com.hubstream.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="parametres_ip")
public class ParametresIp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_param_ip")
    private int idParamIp;

    @Column(name="base_url_api_online")
    private String baseUrlApiOnline;

}
