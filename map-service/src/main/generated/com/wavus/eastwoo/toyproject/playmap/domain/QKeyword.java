package com.wavus.eastwoo.toyproject.playmap.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKeyword is a Querydsl query type for Keyword
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKeyword extends EntityPathBase<Keyword> {

    private static final long serialVersionUID = 700600499L;

    public static final QKeyword keyword = new QKeyword("keyword");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QKeyword(String variable) {
        super(Keyword.class, forVariable(variable));
    }

    public QKeyword(Path<? extends Keyword> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKeyword(PathMetadata metadata) {
        super(Keyword.class, metadata);
    }

}

