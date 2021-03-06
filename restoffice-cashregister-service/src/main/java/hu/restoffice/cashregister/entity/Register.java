package hu.restoffice.cashregister.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hu.restoffice.cashregister.domain.RegisterType;
import hu.restoffice.commons.entity.Identity;

/**
 *
 */
@Entity
@Table(name = "registers")
public class Register implements Serializable, Identity {

    private static final long serialVersionUID = 7776161190060164098L;

    public static final String NO_DETAILS = "Register.noDetails";
    @Id
    @SequenceGenerator(name = "REGISTERS_REGISTER_ID_GENERATOR", sequenceName = "REGISTERS_REGISTER_ID_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REGISTERS_REGISTER_ID_GENERATOR")
    @Column(name = "register_id")
    private Long id;

    @Column(name = "register_registration_no", nullable = false)
    private String registrationNo;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "register_type", nullable = false)
    private RegisterType registerType;

    // bi-directional many-to-one association to RegisterClos
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "register")
    private Set<RegisterClose> registerCloses;

    public Register() {
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * @return the regNo
     */
    public String getRegistrationNo() {
        return registrationNo;
    }

    /**
     * @param regNo
     *            the regNo to set
     */
    public void setRegistrationNo(final String regNo) {
        registrationNo = regNo;
    }

    public RegisterType getRegisterType() {
        return registerType;
    }

    public void setRegisterType(final RegisterType registerType) {
        this.registerType = registerType;
    }

    public Set<RegisterClose> getRegisterCloses() {
        return registerCloses;
    }

    public RegisterClose addRegisterClose(final RegisterClose registerClose) {
        getRegisterCloses().add(registerClose);
        registerClose.setRegister(this);

        return registerClose;
    }

    public RegisterClose removeRegisterClose(final RegisterClose registerClose) {
        getRegisterCloses().remove(registerClose);
        registerClose.setRegister(null);

        return registerClose;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Register [id=" + id + ", registrationNo=" + registrationNo + ", registerType=" + registerType
                + ", registerCloses=" + registerCloses + "]";
    }

}