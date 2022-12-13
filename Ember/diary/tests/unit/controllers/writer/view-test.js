import { module, test } from 'qunit';
import { setupTest } from 'diary/tests/helpers';

module('Unit | Controller | writer/view', function (hooks) {
  setupTest(hooks);

  // TODO: Replace this with your real tests.
  test('it exists', function (assert) {
    let controller = this.owner.lookup('controller:writer/view');
    assert.ok(controller);
  });
});
