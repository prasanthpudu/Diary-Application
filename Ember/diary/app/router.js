import EmberRouter from '@ember/routing/router';
import config from 'diary/config/environment';

export default class Router extends EmberRouter {
  location = config.locationType;
  rootURL = config.rootURL;
}

Router.map(function () {
  this.route('writer', function () {
    this.route('datelist');
    this.route('view', { path: '/:date' });
    this.route('stared');
    this.route('editer');
    this.route('biodata');
  });

  this.route('write', function () {});
  this.route('login');
  this.route('register');
  this.route('forgotpassword');
});
