
Quizki.Collection = Backbone.Collection.extend({
		initialize: function() {
			_.extend(this, Backbone.Events);
		},
		put: function(model, throwEvent) {
			// Created this method put, because I couldn't find a way to override add(), so that I could
			//  trigger the 'somethingChanged' event when something was added.
			model.millisecond_id = new Date().getMilliseconds();
			
			// TODO: use a filter, check for models with the same id, and create a new id if necessessary.
			
			this.add(model);
			
			if (throwEvent !== false)
				this.trigger('somethingChanged');
			
			return model.millisecond_id;
		},
		addArray: function(arr, throwEvent) {
			for (var i=0; i<arr.length; i++) {
				this.add(arr[i]);
			}
			
			if (throwEvent !== false)
				this.trigger('somethingChanged');	
		},
		getByMillisecondId: function(millis) {
			return _.filter(this, function (item) { return item.millisecond_id == millis; })[0];
		},
		update :function (millis, attr, value, throwEvent){
			var v = _.filter(this.models, function (item) {return item.attributes.millisecond_id == millis;	})[0];
			
			var map = {};
			map[attr] = value;
			v.set(map);
			
			if (throwEvent !== false)
				this.trigger('somethingChanged'); 
		},
		remove: function(millis, throwEvent) {
			// new list where only those that do not match millis remain, all except the one we've been asked to remove
			this.models = _.reject(this.models, function(item) { return item.attributes.millisecond_id == millis; });
			
			if (throwEvent !== false)
				this.trigger('somethingChanged'); 
		},
		releasePentUpEvents : function() {
			this.trigger('somethingChanged');
		}
		
	});

