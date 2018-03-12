package com.logicaalternativa.monadtransformerandmore.business.impl;

import akka.util.Timeout;
import com.logicaalternativa.monadtransformerandmore.bean.*;
import com.logicaalternativa.monadtransformerandmore.container.Container;
import scala.Option;
import scala.Tuple2;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.Promise;
import scala.concurrent.duration.Duration;
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
import scala.util.Right;
import scala.util.Try;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
        final ExecutionContext ec = ExecutionContexts.global();

        final Future<Either<Error, Book>> book = srvBook.getBook(idBook);
        final Future<Either<Error, Sales>> sales = srvSales.getSales(idBook);
        final Future<Either<Error, Author>> author = book.flatMap(b -> srvAuthor.getAuthor(b.right().get().getIdAuthor()), ec);
        final Future<Iterable<Either<Error, Chapter>>> listChapters =
                book.flatMap(b ->
                Futures.sequence(b.right()
                        .get()
                        .getChapters()
                        .stream()
                        .map(l -> srvChapter.getChapter(l))
                        .collect(Collectors.toList()), ec), ec);


        Timeout timeout = new Timeout(Duration.create(200, "milliseconds"));
        Book bookComp = null;
        try {
            bookComp = Await.result(book, timeout.duration()).right().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Sales salesComp = null;

        try {
            salesComp = Await.result(sales, timeout.duration()).right().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Author authorComp = null;

        try {
            authorComp = Await.result(author, timeout.duration()).right().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Chapter> listChaptersComp = new ArrayList<>();

        Iterable<Either<Error, Chapter>> eitherIterable = null;

        try {
            eitherIterable = Await.result(listChapters, timeout.duration());
        } catch (Exception e) {
            e.printStackTrace();
        }

        eitherIterable.iterator().forEachRemaining(chapEither -> listChaptersComp.add(chapEither.right().get()));

        Summary summary = new Summary(bookComp, listChaptersComp, Optional.of(salesComp) ,authorComp);
        Either<Error, Summary> either = new Right(summary);

        Promise<Either<Error, Summary>> promise = Futures.promise();

        return promise.success(either).future();
    }

}
