package com.logicaalternativa.monadtransformerandmore.business.impl;

import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

import com.logicaalternativa.monadtransformerandmore.bean.*;
import com.logicaalternativa.monadtransformerandmore.business.SrvSummaryContainer;
import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.monad.MonadContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceAuthorContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceBookContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceChapterContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceSalesContainer;
import com.logicaalternativa.monadtransformerandmore.errors.Error;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SrvSummaryContainerImpl implements SrvSummaryContainer<Error> {

    private final ServiceBookContainer<Error> srvBook;
    private final ServiceSalesContainer<Error> srvSales;
    private final ServiceChapterContainer<Error> srvChapter;
    private final ServiceAuthorContainer<Error> srvAuthor;

    private final MonadContainer<Error> m;


    public SrvSummaryContainerImpl(ServiceBookContainer<Error> srvBook,
                                   ServiceSalesContainer<Error> srvSales,
                                   ServiceChapterContainer<Error> srvChapter,
                                   ServiceAuthorContainer<Error> srvAuthor, MonadContainer<Error> m) {
        super();
        this.srvBook = srvBook;
        this.srvSales = srvSales;
        this.srvChapter = srvChapter;
        this.srvAuthor = srvAuthor;
        this.m = m;
    }


    @Override
    public Container<Error, Summary> getSummary(Integer idBook) {

        Container<Error, Book> book = srvBook.getBook(idBook);
        Container<Error, Sales> sales = srvSales.getSales(idBook);
        Container<Error, Author> author = srvAuthor.getAuthor(book.getValue().getIdAuthor());

        List<Chapter> chapters =
                book.getValue().getChapters().stream().map(c -> srvChapter.getChapter(c).getValue()).collect(Collectors.toList());

        Summary summary = new Summary(book.getValue(), chapters, Optional.of(sales.getValue()), author.getValue());

        return Container.value(summary);

    }

}
