var QuestionnaireTableViewModel = function(patientID, questionPrefs) {
	var self = this;
	self.questions = ko.observableArray();
	self.results = ko.observableArray();
	
	self.prefRows = ko.observableArray([]);
	self.prefResRows = ko.observableArray([]);

	var columnCount = $(".resultsTable"+patientID+" tr.result").first().children("td").size();
	//Construct an empty result row
	var innerHTML = "";
	for (var i = 0; i < columnCount; i++) {
		innerHTML = innerHTML + "<td><div></div></td>";
	}
	self.emptyResRow = {id: -1, text: innerHTML};

	self.init = function() {
		var that = this;
		//Load questions data into model
		that.currentQuestionnaireName = "";
		$(".questionTable"+patientID+" div.question").each(function (idx, elem) {
			var qName = elem.getAttribute("questionnaireName");
            if (qName === null) {
//                Question from Conference measurement - do not show a possible preference. Skip to next
                return true;
            }
			//Add a 'questionnaire name' row if we start on new questionnaire
			if (qName != that.currentQuestionnaireName) {
				that.currentQuestionnaireName = qName;
				self.questions.push({id: "-1", text: "--- "+qName});
			}

			//Remove '<b>' tags from question names
			// IE<10 always returns DOM-tags in upper case, Chrome, FF always returns DOM-tags in lower case
            var name = elem.innerHTML
                .replace("<B>", "")
                .replace("<b>", "")
                .replace("</B>", "")
                .replace("</b>", "");

			self.questions.push({id: elem.getAttribute("id"), text: name});
		});
		
		//Load results data into model
		$(".resultsTable"+patientID+" tr.result").each(function (idx, elem) {
			self.results.push({id: elem.getAttribute("name"), text: elem.innerHTML});
		});
		
		//Load preferences
		for (idx in questionPrefs) {
			for (var i = 0; i < self.questions().length; i++) {
				if (questionPrefs[idx] == self.questions()[i].id) {
					var qRow = new questionTableRow(self.questions()[i], self);
					qRow.isLast = false;
					self.prefRows.push(qRow);
					self.prefResRows.push(new resultTableRow(qRow, self));
					break;
				}
			}
		}
		
		//Add last empty
		var qRow = new questionTableRow(undefined, self)
		self.prefRows.push(qRow)
		self.prefResRows.push(new resultTableRow(qRow, self))

		//Let it fly..
		ko.applyBindings(self);
	}
	
	function questionTableRow(questionObj, viewModel) {
		var that = this;
		this.questionObj = ko.observable(questionObj);
		this.isLast = this.questionObj.id == undefined;
		
		this.removed = ko.observable(false); //Flag for resRow to signal remove to itself
		
		this.questionObj.subscribe(function(newValue) {
			//If this was the last row, add new last row
			if (that.isLast) {
				that.isLast = false;
				var qRow = new questionTableRow(undefined, self);
				viewModel.prefRows.push(qRow);
				viewModel.prefResRows.push(new resultTableRow(qRow, self))
			}
		});
		
		this.remove = function() {
			//Remove the question row
			that.removed(true);
			viewModel.prefRows.remove(this);
		}
	}
	
	function resultTableRow(questionRow, viewModel) {
		var that = this;

		this.resultObj = ko.computed(function() {
			var qRow = questionRow.questionObj();
			if(qRow) {
				var rows = self.results();

				for (idx in rows) {
					if (rows[idx].id == qRow.id) {
						return rows[idx]
					}
				}
			} 
			/* If qRow is undefined or no id match, we either
			 * have the last non-selected select or a select 
			 * where a questionnaire name is selected. 
			 * 
			 * For either of these we need an empty result row.
			 */
			return self.emptyResRow; 
		});

		questionRow.removed.subscribe(function(newVal) {
			if (newVal) {
				viewModel.prefResRows.remove(that);
			}
		});
	}
	
	//Template helper methods
	self.notLastRow = function(elem) {
		return elem.questionObj() != undefined;
	}
	
	self.getQuestionID = function(elem) {
		var id = "-1";
		if (elem.questionObj() != undefined) {id = elem.questionObj().id;}
		return id;
	}
};
		 
	