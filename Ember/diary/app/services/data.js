import Service from '@ember/service';
import { tracked } from '@glimmer/tracking';
import { A } from '@ember/array';
export default class DataService extends Service {
  @tracked registered = true;
  @tracked searchList;
  @tracked userId;
  @tracked dates;
  @tracked notes;
  @tracked bioData;
  host = 'localhost:8080';
}
