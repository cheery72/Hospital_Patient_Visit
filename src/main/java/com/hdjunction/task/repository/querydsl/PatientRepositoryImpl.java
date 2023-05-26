package com.hdjunction.task.repository.querydsl;

import com.hdjunction.task.domain.Patient;
import com.hdjunction.task.domain.QPatient;
import com.hdjunction.task.dto.PatientWithVisitsResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Objects;

public class PatientRepositoryImpl implements PatientRepositoryCustom{

    private static final QPatient qPatient = QPatient.patient;

    private final JPAQueryFactory queryFactory;

    public PatientRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public PatientWithVisitsResponse findByIdWithVisits(Long patientId) {
        Patient patient = queryFactory
                .selectFrom(qPatient)
                .leftJoin(qPatient.visits)
                .fetchJoin()
                .where(qPatient.deleted.eq(false).and(qPatient.id.eq(patientId)))
                .fetchOne();

        return PatientWithVisitsResponse.fromPatientWithVisitsResponse(Objects.requireNonNull(patient));
    }
}
