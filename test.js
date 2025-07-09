{
	let b = 7;
	function foo() {
		let a = 7;
		{
			let a = 5;
			console.log(a);
		}

		function bar() {}
	}
}

bar();
