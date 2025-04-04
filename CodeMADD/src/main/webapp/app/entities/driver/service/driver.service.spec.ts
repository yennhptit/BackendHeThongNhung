import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDriver } from '../driver.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../driver.test-samples';

import { DriverService, RestDriver } from './driver.service';

const requireRestSample: RestDriver = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
};

describe('Driver Service', () => {
  let service: DriverService;
  let httpMock: HttpTestingController;
  let expectedResult: IDriver | IDriver[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DriverService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Driver', () => {
      const driver = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(driver).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Driver', () => {
      const driver = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(driver).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Driver', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Driver', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Driver', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDriverToCollectionIfMissing', () => {
      it('should add a Driver to an empty array', () => {
        const driver: IDriver = sampleWithRequiredData;
        expectedResult = service.addDriverToCollectionIfMissing([], driver);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(driver);
      });

      it('should not add a Driver to an array that contains it', () => {
        const driver: IDriver = sampleWithRequiredData;
        const driverCollection: IDriver[] = [
          {
            ...driver,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDriverToCollectionIfMissing(driverCollection, driver);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Driver to an array that doesn't contain it", () => {
        const driver: IDriver = sampleWithRequiredData;
        const driverCollection: IDriver[] = [sampleWithPartialData];
        expectedResult = service.addDriverToCollectionIfMissing(driverCollection, driver);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(driver);
      });

      it('should add only unique Driver to an array', () => {
        const driverArray: IDriver[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const driverCollection: IDriver[] = [sampleWithRequiredData];
        expectedResult = service.addDriverToCollectionIfMissing(driverCollection, ...driverArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const driver: IDriver = sampleWithRequiredData;
        const driver2: IDriver = sampleWithPartialData;
        expectedResult = service.addDriverToCollectionIfMissing([], driver, driver2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(driver);
        expect(expectedResult).toContain(driver2);
      });

      it('should accept null and undefined values', () => {
        const driver: IDriver = sampleWithRequiredData;
        expectedResult = service.addDriverToCollectionIfMissing([], null, driver, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(driver);
      });

      it('should return initial array if no Driver is added', () => {
        const driverCollection: IDriver[] = [sampleWithRequiredData];
        expectedResult = service.addDriverToCollectionIfMissing(driverCollection, undefined, null);
        expect(expectedResult).toEqual(driverCollection);
      });
    });

    describe('compareDriver', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDriver(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDriver(entity1, entity2);
        const compareResult2 = service.compareDriver(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDriver(entity1, entity2);
        const compareResult2 = service.compareDriver(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDriver(entity1, entity2);
        const compareResult2 = service.compareDriver(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
