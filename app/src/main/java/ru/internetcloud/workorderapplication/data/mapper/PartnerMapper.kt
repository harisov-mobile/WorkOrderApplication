package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.PartnerDbModel
import ru.internetcloud.workorderapplication.data.network.dto.PartnerDTO
import ru.internetcloud.workorderapplication.domain.catalog.Partner

class PartnerMapper {

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

    fun fromDbModelToEntity(partnerDbModel: PartnerDbModel?): Partner? {
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

    fun fromDbModelToEntityWithoutNull(partnerDbModel: PartnerDbModel): Partner {
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
        fromDbModelToEntityWithoutNull(it)
    }

    fun fromListEntityToListDbModel(list: List<Partner>) = list.map {
        fromEntityToDbModel(it)
    }
}
