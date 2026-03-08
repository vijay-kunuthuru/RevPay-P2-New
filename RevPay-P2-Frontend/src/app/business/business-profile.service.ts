import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../shared/models/models';
import { environment } from '../shared/constants';

export interface BusinessProfileDTO {
    profileId: number;
    businessName: string;
    businessType: string;
    taxId: string;
    address: string;
    isVerified: boolean;
}

@Injectable({
    providedIn: 'root'
})
export class BusinessProfileService {
    private apiUrl = `${environment.apiUrl}/business/profile`;

    constructor(private http: HttpClient) { }

    getMyProfile(): Observable<ApiResponse<BusinessProfileDTO>> {
        return this.http.get<ApiResponse<BusinessProfileDTO>>(`${this.apiUrl}/me`);
    }
}
