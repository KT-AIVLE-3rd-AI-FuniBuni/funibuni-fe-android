package com.aivle.data.mapper

import com.aivle.data.entity.waste.LargeCategoryResultEntity
import com.aivle.data.entity.waste.SmallCategoryResultEntity
import com.aivle.data.entity.waste.WasteClassificationDocumentEntity
import com.aivle.data.entity.waste.WasteClassificationRankEntity
import com.aivle.data.entity.waste.WasteDisposalApplyDetailEntity
import com.aivle.data.entity.waste.WasteDisposalApplyEntity
import com.aivle.data.entity.waste.WasteSpecEntity
import com.aivle.domain.model.user.User
import com.aivle.domain.model.waste.LargeCategoryResult
import com.aivle.domain.model.waste.SmallCategoryResult
import com.aivle.domain.model.waste.WasteClassificationDocument
import com.aivle.domain.model.waste.WasteClassificationRank
import com.aivle.domain.model.waste.WasteDisposalApply
import com.aivle.domain.model.waste.WasteDisposalApplyDetail
import com.aivle.domain.model.waste.WasteSpec

fun WasteClassificationDocumentEntity.toModel() =
    WasteClassificationDocument(
        image_title, image_url, labels.map { it.toModel() },
        first_large_category_name, waste_id, user,
        first_large_category_waste_specs.map { it.toModel() }, all_waste_specs.map { it.toModel() }
    )

fun WasteClassificationRankEntity.toModel() =
    WasteClassificationRank(large_category.toModel(), small_category?.toModel())

fun LargeCategoryResultEntity.toModel() =
    LargeCategoryResult(index_large_category, large_category_name, probability)

fun SmallCategoryResultEntity.toModel() =
    SmallCategoryResult(index_small_category, small_category_name, probability)

fun WasteSpecEntity.toModel() =
    WasteSpec(
        waste_spec_id, index_large_category, index_small_category, city, district,
        top_category, large_category, small_category, size_range, is_exists_small_cat_model, type, fee,
    )

/* WasteDisposalApply */
fun WasteDisposalApplyEntity.toModel() = WasteDisposalApply(
    waste_id, waste_spec_id, image_title, image_url, postal_code, address_full_lend, address_full_street,
    address_city, address_district, address_dong, address_detail, disposal_location ?: "", disposal_datetime, memo,
)
fun WasteDisposalApply.toEntity() = WasteDisposalApplyEntity(
    waste_id, waste_spec_id, image_title, image_url, postal_code, address_full_lend, address_full_street,
    address_city, address_district, address_dong, address_detail, disposal_location, disposal_datetime, memo,
)
/* WasteDisposalApplyDetail */
fun WasteDisposalApplyDetailEntity.toModel() = WasteDisposalApplyDetail(
    waste_id, apply_binary, user.toModel(), waste_spec.toModel(), image_title, image_url, postal_code, address_full_lend, address_full_street,
    address_city, address_district, address_dong, address_detail, disposal_location, disposal_datetime, memo, created_at,
)