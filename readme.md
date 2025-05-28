
# 系统模块设计文档

## 一、基础功能模块（物资新建）

### 物资信息录入界面类
**用途**：前端提交新建物资的表单数据  
**JSON字段**：
```json
{
  "materialName": "物资名称（字符串，必填）",
  "categoryId": "分类ID（数值）",
  "specifications": {
    "品牌": "Dell",  /* 动态键值对，根据分类属性扩展 */
    "内存": "16GB"
  },
  "description": "描述文本（可选）"
}
```

### 分类树形接口返回类
**用途**：展示层级分类结构供前端选择  
**JSON字段**：
```json
{
  "id": 1024,
  "name": "电子设备",
  "children": [
    {
      "id": 2048,
      "name": "笔记本电脑",
      "children": []  /* 嵌套结构实现多级树形 */
    }
  ]
}
```

### 表单验证结果类
**用途**：返回前端表单校验错误信息  
**JSON字段**：
```json
{
  "isValid": false,
  "errors": [
    {
      "field": "materialName",
      "message": "物资名称不能为空"
    },
    {
      "field": "specifications.品牌",
      "message": "品牌必须填写"
    }
  ]
}
```

### 操作日志记录类
**用途**：展示物资操作历史记录  
**JSON字段**：
```json
{
  "materialId": 123,
  "operator": "admin",
  "action": "CREATE",
  "operateTime": "2025-05-23 14:30:00"
}
```

---

## 二、飞书审批模块

### 审批详情弹窗数据类
**用途**：显示审批单中的申请人及物资清单  
**JSON字段**：
```json
{
  "applicant": "张三",
  "applyTime": "2025-05-20 10:00:00",
  "materials": [
    {
      "id": 123,
      "name": "MacBook Pro",
      "quantity": 2
    }
  ]
}
```

### 飞书消息推送请求类
**用途**：向飞书发送审批通知的消息内容  
**JSON字段**：
```json
{
  "approvalId": "AP-20250520001",
  "messageType": "CARD",
  "content": {
    "title": "物资借用审批通知",
    "body": "申请人：张三\n申请物资：MacBook Pro x2"
  }
}
```

### 审批状态同步类
**用途**：存储审批状态与飞书消息的关联关系  
**JSON字段（数据库存储）**：
```json
{
  "approvalId": "AP-20250520001",
  "feishuMessageId": "msg_abcd1234",
  "status": "APPROVED"
}
```

---

## 三、分类管理模块

### 分类树形节点类
**用途**：可视化编辑器的节点渲染  
**JSON字段**：
```json
{
  "id": 1024,
  "label": "办公设备",
  "children": [
    {
      "id": 2048,
      "label": "电脑",
      "children": []  /* 支持无限层级嵌套 */
    }
  ]
}
```

### 节点拖拽请求类
**用途**：接收前端拖拽后的节点位置变化  
**JSON字段**：
```json
{
  "draggedNodeId": 2048,
  "targetParentId": 1024,  /* 拖拽后的父节点ID */
  "newIndex": 0  /* 在父节点中的排序位置 */
}
```

### 分类属性扩展配置类
**用途**：定义分类可添加的自定义字段规则  
**JSON字段**：
```json
{
  "categoryId": 1024,
  "attributeName": "保修期限",
  "dataType": "DATE",
  "required": true
}
```

### 删除校验结果类
**用途**：返回分类是否允许删除及关联物资列表  
**JSON字段**：
```json
{
  "canDelete": false,
  "blockReason": "存在关联物资",
  "relatedMaterials": ["电脑-001", "打印机-003"]
}
```

---

## 关键设计原则

- **动态扩展性**  
  物资规格（`specifications`）、分类属性（`attributeName`）使用键值对结构，支持动态字段

- **树形结构通用性**  
  分类树（`children`嵌套）与审批物资清单（`materials`数组）均采用标准化的层级数据格式

- **状态一致性**  
  审批模块中 `approvalId` 与 `feishuMessageId` 双向绑定，确保状态同步

- **错误明确性**  
  表单验证返回具体错误字段及提示，便于前端定位问题
