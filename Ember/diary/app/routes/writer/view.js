import Route from '@ember/routing/route';

export default class WriterViewRoute extends Route {
  model(params) {
    let { date } = params;
    return date;
  }
}
