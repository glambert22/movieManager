package com.secureally.demo.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.secureally.demo.exceptions.MovieManagerException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ResponsePage<T> {

    @JsonIgnore
    private List<T> content;

    private Data<T> data;

    @JsonInclude(Include.NON_NULL)
    private ApplicationError error;

    @JsonInclude(Include.NON_NULL)
    private Meta _meta;

    public ResponsePage(List content, Pageable pageable, long total) {
        this.data = new Data<>();
        this._meta = new Meta();
        this.data.setEntities( content );
        this._meta.offset = pageable.getPageNumber();
        this._meta.limit = pageable.getPageSize();
        this._meta.numberOfElements = (int) total;
        this._meta.sort = pageable.getSort();
    }

    public ResponsePage(T obj, Pageable pageable, boolean hasNext) {
        this.data = new Data<>();
        this._meta = new Meta();
        this.data.setEntity(obj);
        this._meta.offset = pageable.getPageNumber();
        this._meta.limit = pageable.getPageSize();
        this._meta.nextPage = hasNext;
    }

    public ResponsePage(List content) {

        this.data = new Data();
        this._meta = new Meta();

        this.data.setEntities( content );
        this._meta.limit = content.size();
    }

    public ResponsePage(List<T> content, long totalElements, int offset, int size, String link) {
        this(content);
        this._meta.offset = offset;
        this._meta.totalPages =  size == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) size);
        this._meta.numberOfElements = content.size();
        this._meta.totalElements = totalElements;
        this._meta.previousPage = offset > 0 && content.size() > 0;
        this._meta.first = !(offset > 0 && content.size() > 0);
        this._meta.nextPage = totalElements > offset + size;
        this._meta.lastPage = !this._meta.nextPage;
        if (this._meta.nextPage) {
            this._meta.next = link;
        }
    }

    public ResponsePage(Page page) {
        this.data = new Data<>();
        this._meta = new Meta();

        this.data.setEntities( page.getContent() );
        this._meta.offset = page.getNumber();
        this._meta.limit = page.getSize();
        this._meta.totalPages = page.getTotalPages();
        this._meta.pageNumber = page.getNumber();
        this._meta.numberOfElements = page.getNumberOfElements();
        this._meta.totalElements = page.getTotalElements();
        this._meta.previousPage = page.hasPrevious();
        this._meta.first = page.isFirst();
        this._meta.nextPage = page.hasNext();
        this._meta.lastPage = page.isLast();
        this._meta.sort = page.getSort();
    }

    public ResponsePage(Slice slice) {
        this.data = new Data<>();
        this._meta = new Meta();

        this.data.setEntities(slice.getContent());
        this._meta.offset = slice.getNumber();
        this._meta.limit = slice.getSize();
        this._meta.numberOfElements = slice.getNumberOfElements();
        this._meta.previousPage = slice.hasPrevious();
        this._meta.first = slice.isFirst();
        this._meta.nextPage = slice.hasNext();
        this._meta.lastPage = slice.isLast();
        this._meta.sort = slice.getSort();
    }

    public ResponsePage(T obj) {
        this.data = new Data<>();
        this.data.setEntity( obj );
    }

    public ResponsePage() {
        this.data = new Data<>();
        this.data.setEntity("No data found");
    }

    public ResponsePage(int status, MovieManagerException te) {
        error = ApplicationError.of(status, te, null);
    }

    public ResponsePage(int status, MovieManagerException te, List<AppliationConstraintError> errors) {
        error = ApplicationError.of(status, te, errors);
    }

    @Setter
    @Getter
    public class Meta {

        private Integer offset;
        private Integer limit;
        private Integer pageNumber;
        private Integer totalPages;
        private Integer numberOfElements;
        private Long totalElements;
        private Boolean previousPage;
        private Boolean first;
        private Boolean nextPage;
        private Boolean lastPage;

        @JsonInclude(Include.NON_NULL)
        private String prev;

        @JsonInclude(Include.NON_NULL)
        private String next;

        @JsonInclude(Include.NON_NULL)
        private String last;

        @JsonInclude(Include.NON_NULL)
        private Sort sort;
    }
}
