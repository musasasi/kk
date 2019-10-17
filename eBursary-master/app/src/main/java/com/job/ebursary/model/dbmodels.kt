package com.job.ebursary.model

import com.google.firebase.Timestamp

data class ApplicationModel(val name: String? = null, val admission: String? = null, val idnumber: String? = null, val county: String? = null,
                            val phone: String? = null, val address: String? = null, val dateofbirth: String? = null,
                            val amountrequest: String? = null,
                            val time: Timestamp? = null,
                            val school: String? = null)