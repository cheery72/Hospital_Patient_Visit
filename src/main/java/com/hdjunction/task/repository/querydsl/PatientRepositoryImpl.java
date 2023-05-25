package com.hdjunction.task.repository.querydsl;

import com.hdjunction.task.domain.QPatient;
import com.hdjunction.task.domain.QVisit;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

public class PatientRepositoryImpl implements PatientRepositoryCustom{

    private static final QPatient qPatient = QPatient.patient;
    private static final QVisit qVisit = QVisit.visit;

    private final JPAQueryFactory queryFactory;

    public PatientRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

}
