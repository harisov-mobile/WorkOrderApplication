package ru.internetcloud.workorderapplication.presentation.workorder.detail.partner

import ru.internetcloud.workorderapplication.domain.catalog.Partner

data class PartnerWrapper(val partner: Partner, val isSelected: Boolean = false) {
}