package ru.internetcloud.workorderapplication.common.data.mapper

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.model.PartnerDbModel
import ru.internetcloud.workorderapplication.common.data.network.dto.PartnerDTO
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Partner

class PartnerMapper @Inject constructor() {

    fun fromDtoToEntity(partnerDTO: PartnerDTO): Partner {
        return Partner(
            id = partnerDTO.id,
            code1C = partnerDTO.code1C,
            name = partnerDTO.name,
            fullName = partnerDTO.fullName,
            inn = partnerDTO.inn,
            kpp = partnerDTO.kpp
        )
    }

    fun fromDbModelToEntityWithNull(partnerDbModel: PartnerDbModel?): Partner? {
        var result: Partner? = null
        if (partnerDbModel != null) {
            result = Partner(
                id = partnerDbModel.id,
                code1C = partnerDbModel.code1C,
                name = partnerDbModel.name,
                fullName = partnerDbModel.fullName,
                inn = partnerDbModel.inn,
                kpp = partnerDbModel.kpp
            )
        }
        return result
    }

    fun fromDbModelToEntity(partnerDbModel: PartnerDbModel): Partner {
        return Partner(
            id = partnerDbModel.id,
            code1C = partnerDbModel.code1C,
            name = partnerDbModel.name,
            fullName = partnerDbModel.fullName,
            inn = partnerDbModel.inn,
            kpp = partnerDbModel.kpp
        )
    }

    fun fromEntityToDbModel(partner: Partner): PartnerDbModel {
        return PartnerDbModel(
            id = partner.id,
            code1C = partner.code1C,
            name = partner.name,
            fullName = partner.fullName,
            inn = partner.inn,
            kpp = partner.kpp
        )
    }

    fun fromListDtoToListEntity(list: List<PartnerDTO>) = list.map {
        fromDtoToEntity(it)
    }

    fun fromListDbModelToListEntity(list: List<PartnerDbModel>) = list.map {
        fromDbModelToEntity(it)
    }

    fun fromListEntityToListDbModel(list: List<Partner>) = list.map {
        fromEntityToDbModel(it)
    }
}
