####cache中间件

##### 基于Spring-data-redis 扩展 cache 中间件

#### 使用注解方式
	//增加方法级缓存注解
	@Cached(params = "0", expire = 5)
	public int get(int id, int def) {
		//TODO ...
    	}
    		//清除缓存
    		@CacheEvicted(params = {"0"})
    		public boolean clear(int i) {
    			//TODO ...
    		}
		//多个参数使用方法
		@Cached(key = "getList", params = "0,1,2", expire = 10)
		public Map<String, Object> getList(String name, int start, int endIndex) {
		    //TODO ...
		}
	


#### 使用编程式方式（支持二级缓存）
```
	@Autowired
  	private CacheFactory cacheFactory;
  	//定义自己的枚举参考：com.lyh.cache.auto.cache.SimpleCacheType
  	//将需要处理缓存的代码包裹在业务处理代码中即可。
  	String value = cacheFactory.createKey(SimpleCacheType.TEST_HASH_SET, param)
          .setGroup($userId)//可选设置（可根据用户ID做缓存分组，请清除缓存时，可以自动清除掉该用户下的缓存）
          .doExecute(() -> {
            	return "value";//业务处理代码
          });
	//清除缓存示例
	cacheFactory.createKey(SimpleCacheType.TEST_HASH_SET).setGroup(1234).delCache();
```	

##### 后续实现todolist
	将二级缓存API集成到注解cache中
