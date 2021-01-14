import Vue from 'vue'

const fields = Vue.resource('/api/fields{/id}')

export default {
    getAll:    () => fields.get({}),
    getActive: () => fields.get({ id: 'active' }),
    save:   field => fields.save({}, field),
    update: field => fields.update({ id: field.id }, field),
    remove:    id => fields.remove({ id }),
}