import request from '@/utils/request'

// 查询用户收益率规则配置列表
export function listUserYieldRateConfig(query) {
  return request({
    url: '/xms/userYieldRateConfig/list',
    method: 'get',
    params: query
  })
}

// 查询用户收益率规则配置详细
export function getUserYieldRateConfig(id) {
  return request({
    url: '/xms/userYieldRateConfig/' + id,
    method: 'get'
  })
}

// 新增用户收益率规则配置
export function addUserYieldRateConfig(data) {
  return request({
    url: '/xms/userYieldRateConfig',
    method: 'post',
    data: data
  })
}

// 修改用户收益率规则配置
export function updateUserYieldRateConfig(data) {
  return request({
    url: '/xms/userYieldRateConfig',
    method: 'put',
    data: data
  })
}

// 删除用户收益率规则配置
export function delUserYieldRateConfig(id) {
  return request({
    url: '/xms/userYieldRateConfig/' + id,
    method: 'delete'
  })
}
