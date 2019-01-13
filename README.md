####cache中间件

##### 基于Spring-data-redis 扩展 cache 中间件

#### 使用注解方式
	```java
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
	
	```