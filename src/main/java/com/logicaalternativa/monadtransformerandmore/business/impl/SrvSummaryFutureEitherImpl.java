package com.logicaalternativa.monadtransformerandmore.business.impl;

import com.logicaalternativa.monadtransformerandmore.bean.*;
import scala.Tuple2;
import scala.concurrent.Future;
import scala.concurrent.Promise;
import scala.util.Either;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;

import com.logicaalternativa.monadtransformerandmore.business.SrvSummaryFutureEither;
import com.logicaalternativa.monadtransformerandmore.errors.Error;
import com.logicaalternativa.monadtransformerandmore.monad.MonadFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceAuthorFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceBookFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceChapterFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceSalesFutEither;
import com.logicaalternativa.monadtransformerandmore.util.Java8;

import java.util.List;
import java.util.Optional;

import static com.logicaalternativa.monadtransformerandmore.util.Java8.*;
import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

public class SrvSummaryFutureEitherImpl implements SrvSummaryFutureEither<Error> {

    private final ServiceBookFutEither<Error> srvBook;
    private final ServiceSalesFutEither<Error> srvSales;
    private final ServiceChapterFutEither<Error> srvChapter;
    private final ServiceAuthorFutEither<Error> srvAuthor;

    private final MonadFutEither<Error> m;


    public SrvSummaryFutureEitherImpl(ServiceBookFutEither<Error> srvBook,
                                      ServiceSalesFutEither<Error> srvSales,
                                      ServiceChapterFutEither<Error> srvChapter,
                                      ServiceAuthorFutEither<Error> srvAuthor,
                                      MonadFutEither<Error> m) {
        super();
        this.srvBook = srvBook;
        this.srvSales = srvSales;
        this.srvChapter = srvChapter;
        this.srvAuthor = srvAuthor;
        this.m = m;
    }

    @Override
    public Future<Either<Error, Summary>> getSummary(Integer idBook) {

        final Future<Either<Error, Book>> book = srvBook.getBook(idBook);
        final Future<Either<Error, Sales>> sales = srvSales.getSales(idBook);

        Future<Tuple2<Either<Error, Book>, Either<Error, Sales>>> zipBook = book.zip(sales);



        return null;
    }

}
