package application.upload.model

import javax.persistence.*


@Entity
class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    val id: Long? = null
    var name: String? = null
}
