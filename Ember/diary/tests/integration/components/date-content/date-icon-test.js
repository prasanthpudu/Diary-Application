import { module, test } from 'qunit';
import { setupRenderingTest } from 'diary/tests/helpers';
import { render } from '@ember/test-helpers';
import { hbs } from 'ember-cli-htmlbars';

module('Integration | Component | date-content/date-icon', function (hooks) {
  setupRenderingTest(hooks);

  test('it renders', async function (assert) {
    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.set('myAction', function(val) { ... });

    await render(hbs`<DateContent::DateIcon />`);

    assert.dom(this.element).hasText('');

    // Template block usage:
    await render(hbs`
      <DateContent::DateIcon>
        template block text
      </DateContent::DateIcon>
    `);

    assert.dom(this.element).hasText('template block text');
  });
});
