import { module, test } from 'qunit';
import { setupTest } from 'diary/tests/helpers';

module('Unit | Route | writer/view', function (hooks) {
  setupTest(hooks);

  test('it exists', function (assert) {
    let route = this.owner.lookup('route:writer/view');
    assert.ok(route);
  });
});
