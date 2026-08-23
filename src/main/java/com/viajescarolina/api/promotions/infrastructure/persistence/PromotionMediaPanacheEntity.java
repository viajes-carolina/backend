package com.viajescarolina.api.promotions.infrastructure.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "promotion_media")
public class PromotionMediaPanacheEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "promotion_id", nullable = false)
    public Long promotionId;

    @Column(name = "media_asset_id", nullable = false)
    public Long mediaAssetId;

    @Column(name = "display_order", nullable = false)
    public Integer displayOrder = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;
}
