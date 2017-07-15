import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Application } from './application.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ApplicationService {

    private resourceUrl = 'api/applications';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(application: Application): Observable<Application> {
        const copy = this.convert(application);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(application: Application): Observable<Application> {
        const copy = this.convert(application);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Application> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.seedingDate = this.dateUtils
            .convertLocalDateFromServer(entity.seedingDate);
    }

    private convert(application: Application): Application {
        const copy: Application = Object.assign({}, application);
        copy.seedingDate = this.dateUtils
            .convertLocalDateToServer(application.seedingDate);
        return copy;
    }
}
