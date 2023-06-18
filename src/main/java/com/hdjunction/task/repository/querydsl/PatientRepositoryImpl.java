package com.hdjunction.task.repository.querydsl;

import com.hdjunction.task.domain.Patient;
import com.hdjunction.task.domain.QPatient;
import com.hdjunction.task.domain.QVisit;
import com.hdjunction.task.dto.PatientDetailsResponse;
import com.hdjunction.task.dto.PatientWithVisitsResponse;
import com.hdjunction.task.dto.QPatientDetailsResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

public class PatientRepositoryImpl implements PatientRepositoryCustom{

    private static final QPatient qPatient = QPatient.patient;
    private static final QVisit qVisit = QVisit.visit;

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

    @Override
    public Page<PatientDetailsResponse> findByPatientDetails(String searchParams,String content,Pageable pageable) {
        BooleanBuilder booleanBuilder = getBooleanBuilder(searchParams,content);

        List<PatientDetailsResponse> results = queryFactory
                .select(new QPatientDetailsResponse(
                        qPatient.id,
                        qPatient.name,
                        qPatient.registrationNumber,
                        qPatient.genderCode,
                        qPatient.birthDate,
                        qPatient.phoneNumber,
                        qVisit.registrationDateTime.max().stringValue().substring(0,10)
                ))
                .from(qPatient)
                .leftJoin(qPatient.visits, qVisit)
                .where(booleanBuilder.and(qPatient.deleted.eq(false)))
                .groupBy(
                        qPatient.id,
                        qPatient.name,
                        qPatient.registrationNumber,
                        qPatient.genderCode,
                        qPatient.birthDate,
                        qPatient.phoneNumber
                )
                .orderBy(qPatient.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }

    private BooleanBuilder getBooleanBuilder(String searchParams, String content) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (content.isBlank()){
            return booleanBuilder;
        }

        if ("name".equals(searchParams)) {
            booleanBuilder.and(qPatient.name.eq(content));
            return booleanBuilder;
        }

        if ("registration".equals(searchParams)) {
            booleanBuilder.and(qPatient.registrationNumber.eq(content));
            return booleanBuilder;
        }

        if ("birth".equals(searchParams)) {
            booleanBuilder.and(qPatient.birthDate.eq(content));
        }

        return booleanBuilder;

    }
}
