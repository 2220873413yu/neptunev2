import request from '@/utils/request'

// 查询收益加速释放配置列表
export function listAccelerateReleaseConfig(query) {
  return request({
    url: '/xms/accelerateReleaseConfig/list',
    method: 'get',
    params: query
  })
}

// 查询收益加速释放配置详细
export function getAccelerateReleaseConfig(id) {
  return request({
    url: '/xms/accelerateReleaseConfig/' + id,
    method: 'get'
  })
}

// 新增收益加速释放配置
export function addAccelerateReleaseConfig(data) {
  return request({
    url: '/xms/accelerateReleaseConfig',
    method: 'post',
    data: data
  })
}

// 修改收益加速释放配置
export function updateAccelerateReleaseConfig(data) {
  return request({
    url: '/xms/accelerateReleaseConfig',
    method: 'put',
    data: data
  })
}

// 删除收益加速释放配置
export function delAccelerateReleaseConfig(id) {
  return request({
    url: '/xms/accelerateReleaseConfig/' + id,
    method: 'delete'
  })
}
