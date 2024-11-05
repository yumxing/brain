Ext.define('StudentRecord', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'int'},
        {name: 'code', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'sex', type: 'int'},
        {name: 'age', type: 'int'},
        {name: 'political', type: 'string'},
        {name: 'origin', type: 'string'},
        {name: 'professional', type: 'string'},
    ]
});

const store = Ext.create('Ext.data.Store', {
    pageSize: 15,
    proxy: {
        type: 'ajax',
        url: 'student/list.do',
        reader: {
            type: 'json',
            totalProperty: 'totalCount',
            root: 'result',
            idProperty: 'id'
        }
    },
    model: StudentRecord,
    remoteSort: true
});

const sexRenderer = function (value) {
    if (value === 1) {
        return '<span style="color:red;font-weight:bold;">男</span>';
    } else if (value === 2) {
        return '<span style="color:green;font-weight:bold;">女</span>';
    }
};

const columns = [
    {header: 'ID', dataIndex: 'id', hidden: true},
    {header: '学号', dataIndex: 'code'},
    {header: '姓名', dataIndex: 'name'},
    {header: '性别', dataIndex: 'sex', renderer: sexRenderer},
    {header: '年龄', dataIndex: 'age'},
    {header: '政治面貌', dataIndex: 'political'},
    {header: '籍贯', dataIndex: 'origin'},
    {header: '所属系', dataIndex: 'professional'},
];

const comboSex = new Ext.form.ComboBox({
    fieldLabel: '性别',
    name: 'sex',
    hiddenName: 'sex',
    store: Ext.create('Ext.data.ArrayStore', {
        fields: ['value', 'text'],
        data: [[1, '男'], [2, '女']]
    }),
    emptyText: '请选择',
    mode: 'local',
    triggerAction: 'all',
    valueField: 'value',
    displayField: 'text',
    editable: false,    //确保用户不能手动编辑 ComboBox 的值
    forceSelection: true    //确保用户只能选择预定义的选项，防止输入无效值
});

const comboPolitical = new Ext.form.ComboBox({
    fieldLabel: '政治面貌',
    name: 'political',
    hiddenName: 'political',
    store: new Ext.data.SimpleStore({
        fields: ['text'],
        data: [['群众'], ['团员'], ['党员']]
    }),
    emptyText: '请选择',
    mode: 'local',
    valueField: 'text',
    displayField: 'text',
    triggerAction: 'all',
    //readOnly: true
});

// grid start
const grid = Ext.create('Ext.grid.GridPanel', {
    title: '学生信息列表',
    region: 'center',
    loadMask: true,
    store: store,
    columns: columns,
    forceFit: true,
    bbar: Ext.create('Ext.PagingToolbar', {
        pageSize: 15,
        store: store,
        displayInfo: true
    })
});
// grid end

// form start
const form = Ext.create('Ext.form.FormPanel', {
    id: 'formStudent',
    title: '编辑学生信息',
    region: 'east',
    frame: true,
    width: 300,
    authHeight: true,
    labelAlign: 'right',
    labelWidth: 60,
    defaultType: 'textfield',
    defaults: {
        width: 200,
        allowBlank: false
    },
    items: [{
        fieldLabel: 'ID',
        name: 'id',
        allowBlank: true,
        hidden: true,
        readOnly: true
    }, {
        fieldLabel: '学号',
        name: 'code'
    }, {
        fieldLabel: '姓名',
        name: 'name'
    }, {
        xtype: 'numberfield',
        fieldLabel: '年龄',
        name: 'age',
        allowNegative: false,
        allowDecimals: false,
        //decimalPrecision: 4,
        minValue: 0,
        maxValue: 150,
        maskRe: /\d/
    }, comboSex, comboPolitical, {
        fieldLabel: '籍贯',
        name: 'origin'
    }, {
        fieldLabel: '所属系',
        name: 'professional'
    }],
    buttons: [{
        id: 'buttonSave',
        text: '添加',
        handler: submitForm
    }, {
        id: 'buttonClear',
        text: '清空',
        handler: function () {
            const formInstance = checkFormValidity(form);
            if (!formInstance) {
                return;
            }
            formInstance.reset();
            Ext.getCmp('buttonSave').setText('添加');
            //form.buttons[0].setText('添加');
        }
    }, {
        id: 'buttonDel',
        text: '删除',
        handler: function () {
            const formInstance = checkFormValidity(form);
            if (!formInstance) {
                return;
            }
            const id = formInstance.findField("id").getValue();
            if (id === '') {
                Ext.Msg.alert('提示', '请选择需要删除的信息。');
            } else {
                Ext.Ajax.request({
                    url: 'student/remove.do',
                    success: function (response) {
                        const json = Ext.decode(response.responseText);
                        if (json.success) {
                            Ext.Msg.alert('消息', json.msg, function () {
                                grid.getStore().reload();
                                formInstance.reset();
                                //form.buttons[0].setText('添加');
                                Ext.getCmp('buttonSave').setText('添加');
                            });
                        }
                    },
                    failure: function () {
                        Ext.Msg.alert('错误', "删除失败");
                    },
                    params: "id=" + id
                });
            }
        }
    }]
});
// form end

Ext.onReady(function () {
    store.load();
    Ext.create('Ext.Viewport', {
        layout: 'border',
        items: [
            {region: 'north', contentEl: 'head'},
            grid, form,
            {region: 'south', contentEl: 'foot'}
        ]
    });

    //单击修改信息开始
    grid.on('itemclick', function (view, record) {
        form.getForm().loadRecord(record);
        Ext.getCmp('buttonSave').setText('修改');
    });
    //单击修改信息结束
});


/**
 * form表单，确保 reset() 方法存在
 * @param form
 * @returns {{reset}|*|boolean}
 */
function checkFormValidity(form) {
    if (!form || !form.getForm) {
        console.error('Form or getForm method not found');
        return false;
    }
    const formInstance = form.getForm();
    if (!formInstance || !formInstance.reset) {
        console.error('Form instance or reset method not found');
        return false;
    }
    return formInstance;
}

/**
 * 表单提交事件成功后，根据返回结果，执行其他操作
 * @param action
 * @param formInstance
 */
function handleSuccess(action, formInstance) {
    if (action.result.success) {
        let msg = action.result.msg;
        Ext.MessageBox.alert('消息', msg, function () {
            grid.getStore().reload();
            formInstance.reset();
            Ext.getCmp('buttonSave').setText('添加');
        });
    }
}

/**
 * 表单提交事件
 */
function submitForm() {
    const formInstance = checkFormValidity(form);
    if (!formInstance) {
        return;
    }
    if (!formInstance.isValid()) {
        return;
    }
    const url = "student/save.do";
    let failMsg = "操作失败";
    if (formInstance.findField("id").getValue() === "") {
        failMsg = "添加失败";
    } else {
        failMsg = "修改失败";
    }

    formInstance.submit({
        url: url,
        success: function (f, action) {
            handleSuccess(action, formInstance);
        },
        failure: function () {
            Ext.Msg.alert('错误', failMsg);
        }
    });
}