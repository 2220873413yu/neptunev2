import request from '@/utils/request'

// 查询boomai收益线性释放计划列表
export function listBoomaiReleasePlan(query) {
  return request({
    url: '/xms/boomaiReleasePlan/list',
    method: 'get',
    params: query
  })
}

// 查询boomai收益线性释放计划详细
export function getBoomaiReleasePlan(id) {
  return request({
    url: '/xms/boomaiReleasePlan/' + id,
    method: 'get'
  })
}

// 新增boomai收益线性释放计划
export function addBoomaiReleasePlan(data) {
  return request({
    url: '/xms/boomaiReleasePlan',
    method: 'post',
    data: data
  })
}

// 修改boomai收益线性释放计划
export function updateBoomaiReleasePlan(data) {
  return request({
    url: '/xms/boomaiReleasePlan',
    method: 'put',
    data: data
  })
}

// 删除boomai收益线性释放计划
export function delBoomaiReleasePlan(id) {
  return request({
    url: '/xms/boomaiReleasePlan/' + id,
    method: 'delete'
  })
}

// 查询合并来源详细
export function getBoomaiReleasePlanSource(remark) {
  return request({
    url: '/xms/boomaiReleasePlan/source',
    method: 'get',
    params: { remark }
  })
}